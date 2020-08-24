/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/8/23/023   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.graphic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sand.common.util.lang3.StringUtil;
import com.sand.graphic.entity.ArrearsInfo;
import com.sand.graphic.entity.DailyDeposit;
import com.sand.graphic.entity.InvestSupervise;
import com.sand.graphic.entity.MerchantInfo;
import com.sand.graphic.entity.PublicOpinion;
import com.sand.graphic.mapper.ArrearsInfoMapper;
import com.sand.graphic.mapper.DailyDepositMapper;
import com.sand.graphic.mapper.InvestSuperviseMapper;
import com.sand.graphic.mapper.MerchantInfoMapper;
import com.sand.graphic.mapper.PublicOpiniionMapper;
import com.sand.graphic.service.IMerchantInfoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/8/23/023 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Slf4j
@Service
public class MerchantInfoServiceImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements IMerchantInfoService {
  @Value("${risk.evaluation-time}")
  private int evaluationtime;
  @Autowired
  private DailyDepositMapper dailyDepositMapper;
  @Autowired
  private PublicOpiniionMapper publicOpiniionMapper;
  @Autowired
  private InvestSuperviseMapper investSuperviseMapper;
  @Autowired
  private ArrearsInfoMapper arrearsInfoMapper;

  @Override
  public Map<String, Object> view(String merId) {
    // 查询企业基础信息评估结果
    log.info("--------------开始评估企业--------------");
    MerchantInfo merchantInfo = this.getById(merId);
    log.info("--------------查询企业基础信息完成--------------");
    List<DailyDeposit> depositList = dailyDepositMapper.selectList(new QueryWrapper<DailyDeposit>().eq("merId", merId));
    log.info("--------------查询企业日均存款信息完成--------------");
    // 计算各维度得分及合作深度明细得分
    log.info("--------------计算企业综合得分--------------");
    // 查询风险画像
    Map<String, Object> allResult = evaluationMerchant(merchantInfo, depositList);
    // 计算时间区间
    Date date = getTimeInterval();
    log.info("-----------查询企业风险画像-------------------");
    QueryWrapper<PublicOpinion> publicOpinion = new QueryWrapper<>();
    publicOpinion.eq("merId", merId)
        .ge("pubOpinDate", date)
        .orderByDesc("pubOpinDate")
        .last("limit 0, 10");
    // 查询负面舆情
    List<PublicOpinion> opinionList = publicOpiniionMapper.selectList(publicOpinion);
    log.info("-----------查询负面舆情完成-------------------");
    QueryWrapper<InvestSupervise> investSupervise = new QueryWrapper<>();
    investSupervise.eq("merId", merId)
        .ge("inveSupDate", date)
        .orderByDesc("inveSupDate")
        .last("limit 0, 10");
    // 查询投资监督提示
    List<InvestSupervise> invSupList = investSuperviseMapper.selectList(investSupervise);
    log.info("-----------查询投资监督提示完成-------------------");
    // 查询欠费
    List<ArrearsInfo> arrInfoList = arrearsInfoMapper.selectList(new QueryWrapper<ArrearsInfo>()
        .eq("merId", merId)
        .orderByDesc("arrearsNum")
        .last("limit 0, 10"));
    log.info("-----------查询欠费信息完成-------------------");
    // 计算风险雷达图
    List<Map<String, Object>> riskList = evaluationRisk(opinionList, invSupList, arrInfoList);
    allResult.put("riskList", riskList);
    log.info("-----------计算风险画像完成-------------------");
    // 计算总分
    log.info("-----------综合分析-------------------");
    ArrayList<Radar> baseList = (ArrayList<Radar>) allResult.get("baseList");
    int gaugeScore = evaluationScore(baseList);
    // 计算客户等级
    Gauge gauge = getMarketAdvice(gaugeScore);
    String riskAdvice = getRiskAdvice(riskList);
    log.info("风险建议：{}", riskAdvice);
    gauge.setRiskAdvice(riskAdvice);
    // 计算前三个未合作项目
    ArrayList<Radar> noCoopList = (ArrayList<Radar>) allResult.get("noCoopList");
    noCoopList = getnoCoopList(noCoopList);
    log.info("--------计算待加强合作点完成--------------");
    gauge.setList(noCoopList);
    log.info("-----------综合分析完成-------------------");
    allResult.put("gauge", gauge);

    return allResult;
  }

  private Map<String, Object> evaluationMerchant(MerchantInfo merchantInfo, List<DailyDeposit> depositList) {
    Map<String, Object> resultMap = Maps.newHashMap();
    // 企业基本信息
    ArrayList<Radar> baseList = Lists.newArrayList();
    // 合作业务
    ArrayList<Radar> detailList = Lists.newArrayList();
    // 未合作业务
    ArrayList<Radar> noCoopList = Lists.newArrayList();
    String merId = merchantInfo.getMerId();
    // 计算业务规模得分
    double busiScale = merchantInfo.getBusiScale();
    int score = 0;
    if (busiScale < 100000d && busiScale > 0d) {
      score = 20;
    } else if (busiScale < 500000d) {
      score = 40;
    } else if (busiScale < 1000000d) {
      score = 60;
    } else if (busiScale < 2000000d) {
      score = 80;
    } else {
      score = 100;
    }
    log.info("业务规模：{}分", score);
    Radar bs = new Radar(merId, "业务规模", score);
    baseList.add(bs);
    // 计算业务收入得分
    double busiIncome = merchantInfo.getBusiIncome();
    score = 0;
    if (busiIncome < 200000d && busiIncome > 0d) {
      score = 20;
    } else if (busiIncome < 500000d) {
      score = 40;
    } else if (busiIncome < 1000000d) {
      score = 60;
    } else if (busiIncome < 2000000d) {
      score = 80;
    } else {
      score = 100;
    }
    log.info("业务收入：{}分", score);
    Radar bi = new Radar(merId, "业务收入", score);
    baseList.add(bi);
    // 计算产品数量得分
    int productNum = merchantInfo.getProductNum();
    score = 0;
    if (productNum < 5 && productNum > 0) {
      score = 20;
    } else if (productNum < 10) {
      score = 40;
    } else if (productNum < 15) {
      score = 60;
    } else if (productNum < 20) {
      score = 80;
    } else {
      score = 100;
    }
    log.info("产品数量：{}分", score);
    Radar pn = new Radar(merId, "产品数量", score);
    baseList.add(pn);
    // 计算日均存款得分
    double dailyDeposit = depositList.stream()
        .mapToDouble(DailyDeposit::getDailyDeposit)
        .sum();
    score = 0;
    if (dailyDeposit < 10000000d && dailyDeposit > 0d) {
      score = 20;
    } else if (dailyDeposit < 50000000d) {
      score = 40;
    } else if (dailyDeposit < 100000000d) {
      score = 60;
    } else if (dailyDeposit < 200000000d) {
      score = 80;
    } else {
      score = 100;
    }
    log.info("日均存款：{}分", score);
    Radar dd = new Radar(merId, "日均存款", score);
    baseList.add(dd);
    // 计算合作深度得分
    score = 0;
    String platfPCFlag = merchantInfo.getPlatfPCFlag();
    Radar info1 = new Radar(merId, "服务平台PC", 20);
    if (isOpen(platfPCFlag)) {
      score += 20;
      detailList.add(info1);
    } else {
      noCoopList.add(info1);
    }
    String platfAPPFlag = merchantInfo.getPlatfAPPFlag();
    Radar info2 = new Radar(merId, "杭银托管APP", 8);
    if (isOpen(platfAPPFlag)) {
      score += 8;
      detailList.add(info2);
    } else {
      noCoopList.add(info2);
    }
    String platfGZFlag = merchantInfo.getPlatfGZFlag();
    Radar info3 = new Radar(merId, "杭银托管公众号", 7);
    if (isOpen(platfGZFlag)) {
      score += 7;
      detailList.add(info3);
    } else {
      noCoopList.add(info3);
    }
    String perEvaluate = merchantInfo.getPerEvaluate();
    Radar info4 = new Radar(merId, "绩效评估", 10);
    if (isOpen(perEvaluate)) {
      score += 10;
      detailList.add(info4);
    } else {
      noCoopList.add(info4);
    }
    String valuaOutsourc = merchantInfo.getValuaOutsourc();
    Radar info5 = new Radar(merId, "估值外包", 5);
    if (isOpen(valuaOutsourc)) {
      score += 5;
      detailList.add(info5);
    } else {
      noCoopList.add(info5);
    }
    String bankMemo = merchantInfo.getBankMemo();
    Radar info6 = new Radar(merId, "银行间备忘录", 10);
    if (isOpen(bankMemo)) {
      score += 10;
      detailList.add(info6);
    } else {
      noCoopList.add(info6);
    }
    String electrOrder = merchantInfo.getElectrOrder();
    Radar info7 = new Radar(merId, "电子指令直连", 15);
    if (isOpen(electrOrder)) {
      score += 15;
      detailList.add(info7);
    } else {
      noCoopList.add(info7);
    }
    String fileTransfer = merchantInfo.getFileTransfer();
    Radar info8 = new Radar(merId, "文件传输直连", 6);
    if (isOpen(fileTransfer)) {
      score += 6;
      detailList.add(info8);
    } else {
      noCoopList.add(info8);
    }
    String electrCheck = merchantInfo.getElectrCheck();
    Radar info9 = new Radar(merId, "电子对账", 9);
    if (isOpen(electrCheck)) {
      score += 9;
      detailList.add(info9);
    } else {
      noCoopList.add(info9);
    }
    String vitality = merchantInfo.getVitality();
    Radar info10 = new Radar(merId, "参与活跃度", 10);
    if (isOpen(vitality)) {
      score += 10;
      detailList.add(info10);
    } else {
      noCoopList.add(info10);
    }
    log.info("合作深度：{}分", score);
    Radar coo = new Radar(merId, "合作深度", score);
    baseList.add(coo);
    resultMap.put("baseList", baseList);
    resultMap.put("detailList", detailList);
    resultMap.put("noCoopList", noCoopList);

    return resultMap;
  }

  private boolean isOpen(String s) {
    if (StringUtil.isNotBlank(s) && s.equalsIgnoreCase("1")) {
      return true;
    } else {
      return false;
    }
  }

  private Date getTimeInterval() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.YEAR, -evaluationtime);
    log.info("过去{}年的风险数据", evaluationtime);
    return calendar.getTime();
  }

  private List<Map<String, Object>> evaluationRisk(List<PublicOpinion> pubOpinList,
                                                   List<InvestSupervise> invSupList, List<ArrearsInfo> arrInfoList) {
    ArrayList<Map<String, Object>> riskList = Lists.newArrayList();
    int num1 = 0;
    if (!pubOpinList.isEmpty() && pubOpinList.size() > 0) {
      num1 = pubOpinList.size();
    }
    Map<String, Object> map1 = Maps.newHashMap();
    map1.put("name", "负面舆情");
    map1.put("max", num1);
    map1.put("detail", pubOpinList);
    riskList.add(map1);

    int num2 = 0;
    if (!invSupList.isEmpty() && invSupList.size() > 0) {
      num2 = invSupList.size();
    }
    Map<String, Object> map2 = Maps.newHashMap();
    map2.put("name", "投资监督提示");
    map2.put("max", num2);
    map2.put("detail", invSupList);
    riskList.add(map2);

    int num3 = 0;
    if (!arrInfoList.isEmpty() && arrInfoList.size() > 0) {
      for (ArrearsInfo info : arrInfoList) {
        int arrearsNum = info.getArrearsNum();
        num3 += arrearsNum;
      }
    }
    Map<String, Object> map3 = Maps.newHashMap();
    map3.put("name", "欠费");
    map3.put("max", num3);
    map3.put("detail", arrInfoList);
    riskList.add(map3);
    return riskList;
  }

  private int evaluationScore(ArrayList<Radar> baseList) {
    BigDecimal totalScore = new BigDecimal("0.00");
    int basic;
    double weight;
    for (Radar Radar : baseList) {
      String name = Radar.getName();
      basic = Radar.getRadarScore();
      if (name.equals("合作深度")) {
        weight = 0.4d;
      } else if (name.equals("业务规模")) {
        weight = 0.24d;
      } else if (name.equals("业务收入")) {
        weight = 0.18d;
      } else if (name.equals("产品数量")) {
        weight = 0.09d;
      } else {
        weight = 0.09d;
      }
      BigDecimal basicD = new BigDecimal(basic);
      BigDecimal weightD = BigDecimal.valueOf(weight);
      BigDecimal multiply = basicD.multiply(weightD);
      totalScore = multiply.add(totalScore);
    }
    int result = totalScore.setScale(0, BigDecimal.ROUND_DOWN).intValue();
    return result;
  }

  private String getRiskAdvice(int num1, int num2, int num3) {
    String riskAdvice = "";
    int level = 1;
    int i;
    if (num1 < 10) {
      i = 1;
    } else if (num1 <= 50) {
      i = 2;
    } else {
      i = 3;
    }
    level = (i > level) ? i : level;
    if (num2 < 3) {
      i = 1;
    } else if (num2 < 6) {
      i = 2;
    } else {
      i = 3;
    }
    level = (i > level) ? i : level;
    if (num2 < 3) {
      i = 1;
    } else if (num2 < 4) {
      i = 2;
    } else {
      i = 3;
    }
    level = (i > level) ? i : level;
    switch (level) {
      case 1:
        riskAdvice = "暂无/较少负面舆情支持全品类产品";
        break;
      case 2:
        riskAdvice = "适度关注负面舆情支持以非银结算模式为主、银行结算为辅产品";
        break;
      case 3:
        riskAdvice = "密切关注负面舆情跟进风险债券处置进度只支持非银结算模式产品";
        break;
      default:
        break;
    }
    return riskAdvice;
  }

  private String getRiskAdvice(List<Map<String, Object>> riskList) {
    int num1 = 0;
    int num2 = 0;
    int num3 = 0;
    for (Map<String, Object> hashMap : riskList) {
      String name = hashMap.get("name").toString();
      if (name.equals("负面舆情")) {
        num1 = Integer.parseInt(hashMap.get("max").toString());
      } else if (name.equals("投资监督提示")) {
        num2 = Integer.parseInt(hashMap.get("max").toString());
      } else {
        num3 = Integer.parseInt(hashMap.get("max").toString());
      }
    }
    return getRiskAdvice(num1, num2, num3);
  }

  private Gauge getMarketAdvice(int gaugeScore) {
    Gauge guage = new Gauge();
    String marketAdvice = "";
    String name = "";
    if (gaugeScore < 50) {
      name = "黄金客户";
      marketAdvice = "每月周至少联系一次<br>每三个月至少拜访一次<br>" +
          "每月向总行反馈一次进度加强合作深度，提升客户黏性";
    } else if (gaugeScore < 80) {
      name = "铂金客户";
      marketAdvice = "每两周至少联系一次<br>每两个月至少拜访一次<br>" +
          "每月向总行反馈一次进度加强合作深度，提升客户黏性";
    } else {
      name = "钻石客户";
      marketAdvice = "每周至少联系一次<br>每月至少拜访一次<br>" +
          "每两周向总行反馈一次进度加强合作深度，提升客户黏性";
    }
    guage.setName(name);
    log.info("客户等级：" + name);
    guage.setGaugeScore(gaugeScore);
    log.info("综合得分：" + gaugeScore);
    guage.setMarketAdvice(marketAdvice);
    log.info("营销建议：" + marketAdvice);
    return guage;
  }

  private ArrayList<Radar> getnoCoopList(ArrayList<Radar> noCoopList) {
    int size = noCoopList.size();
    if (!noCoopList.isEmpty() && size > 0) {
      Collections.sort(noCoopList);
      if (size < 3) {
        return noCoopList;
      } else {
        ArrayList<Radar> subList = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
          Radar info = noCoopList.get(i);
          subList.add(info);
        }
        return subList;
      }
    }
    return noCoopList;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class Radar implements Comparable<Radar> {
    private String merId;
    private String name;
    private int radarScore;

    @Override
    public int compareTo(Radar o) {
      return o.radarScore - this.radarScore;
    }

    @Override
    public String toString() {
      return this.radarScore + "";
    }
  }

  /**
   * 仪表盘数据显示类
   *
   * @author liusha
   * @date 2020/08/24
   */
  @Data
  @NoArgsConstructor
  public class Gauge {
    private String name;
    private int gaugeScore;
    private String marketAdvice;
    private String riskAdvice;
    private List<Radar> list;
  }
}
