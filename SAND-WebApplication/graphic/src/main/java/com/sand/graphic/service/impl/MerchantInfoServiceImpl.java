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
import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.lang3.DateUtil;
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
import com.sand.graphic.util.ToolUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
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
  /**
   * 企业基本信息
   */
  private static final String BASE_LIST = "baseList";
  /**
   * 合作业务
   */
  private static final String COOP_LIST = "coopList";
  /**
   * 非合作业务
   */
  private static final String NO_COOP_LIST = "noCoopList";

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
    Map<String, Object> allResult = this.evaluationMerchant(merchantInfo, depositList);
    // 计算时间区间
    Date date = DateUtil.addYears(new Date(), -evaluationtime);
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
    List<Map<String, Object>> riskList = this.evaluationRisk(opinionList, invSupList, arrInfoList);
    allResult.put("riskList", riskList);
    log.info("-----------计算风险画像完成-------------------");
    // 计算总分
    log.info("-----------综合分析-------------------");
    List<Radar> baseList = (List<Radar>) allResult.get(BASE_LIST);
    int gaugeScore = this.evaluationScore(baseList);
    // 计算客户等级
    Gauge gauge = this.getMarketAdvice(gaugeScore);
    String riskAdvice = ToolUtil.getRiskAdvice(riskList);
    log.info("风险建议：{}", riskAdvice);
    gauge.setRiskAdvice(riskAdvice);
    // 计算前三个未合作项目
    List<Radar> noCoopList = (List<Radar>) allResult.get(NO_COOP_LIST);
    noCoopList = this.getNoCoopList(noCoopList);
    log.info("--------计算待加强合作点完成--------------");
    gauge.setList(noCoopList);
    log.info("-----------综合分析完成-------------------");
    allResult.put("gauge", gauge);

    return allResult;
  }

  /**
   * 查询风险画像
   *
   * @param merchantInfo 企业信息
   * @param depositList  企业基础数据
   * @return 风险画像
   */
  private Map<String, Object> evaluationMerchant(MerchantInfo merchantInfo, List<DailyDeposit> depositList) {
    Map<String, Object> resultMap = Maps.newHashMap();
    // 企业基本信息
    List<Radar> baseList = Lists.newArrayList();
    String merId = merchantInfo.getMerId();
    int score;
    // 计算业务规模得分
    double businessScale = merchantInfo.getBusiScale();
    List<String> scaleRules = Arrays.asList("0", "100000", "500000", "1000000", "2000000");
    score = ToolUtil.calculateScore(SandConvert.obj2Str(businessScale), scaleRules);
    log.info("业务规模：{}分", score);
    Radar bs = new Radar(merId, "业务规模", score);
    baseList.add(bs);
    // 计算业务收入得分
    double businessIncome = merchantInfo.getBusiIncome();
    List<String> incomeRules = Arrays.asList("0", "200000", "500000", "1000000", "2000000");
    score = ToolUtil.calculateScore(SandConvert.obj2Str(businessIncome), incomeRules);
    log.info("业务收入：{}分", score);
    Radar bi = new Radar(merId, "业务收入", score);
    baseList.add(bi);
    // 计算产品数量得分
    int productNum = merchantInfo.getProductNum();
    List<String> productRules = Arrays.asList("0", "5", "10", "15", "20");
    score = ToolUtil.calculateScore(SandConvert.obj2Str(productNum), productRules);
    log.info("产品数量：{}分", score);
    Radar pn = new Radar(merId, "产品数量", score);
    baseList.add(pn);
    // 计算日均存款得分
    double dailyDeposit = depositList.stream()
        .mapToDouble(DailyDeposit::getDailyDeposit)
        .sum();
    List<String> dailyRules = Arrays.asList("0", "10000000", "50000000", "100000000", "200000000");
    score = ToolUtil.calculateScore(SandConvert.obj2Str(dailyDeposit), dailyRules);
    log.info("日均存款：{}分", score);
    Radar dd = new Radar(merId, "日均存款", score);
    baseList.add(dd);
    // 获取合作深度得分
    List<Radar> coopList = Lists.newArrayList();
    List<Radar> noCoopList = Lists.newArrayList();
    score = this.getTotalScore(merchantInfo, coopList, noCoopList);
    log.info("合作深度：{}分", score);
    Radar coo = new Radar(merId, "合作深度", score);
    baseList.add(coo);
    resultMap.put(BASE_LIST, baseList);
    resultMap.put(COOP_LIST, coopList);
    resultMap.put(NO_COOP_LIST, noCoopList);

    return resultMap;
  }

  /**
   * 获取合作深度总得分
   *
   * @param merchantInfo 企业信息
   * @param coopList     合作业务
   * @param noCoopList   未合作业务
   * @return 总得分
   */
  private int getTotalScore(MerchantInfo merchantInfo, List<Radar> coopList, List<Radar> noCoopList) {
    int score = 0;
    String merId = merchantInfo.getMerId();

    String platformPcFlag = merchantInfo.getPlatfPCFlag();
    Radar info1 = new Radar(merId, "服务平台PC", 20);
    score += this.getSingleScore(platformPcFlag, info1, coopList, noCoopList);

    String platformAppFlag = merchantInfo.getPlatfAPPFlag();
    Radar info2 = new Radar(merId, "杭银托管APP", 8);
    score += this.getSingleScore(platformAppFlag, info2, coopList, noCoopList);

    String platformGzFlag = merchantInfo.getPlatfGZFlag();
    Radar info3 = new Radar(merId, "杭银托管公众号", 7);
    score += this.getSingleScore(platformGzFlag, info3, coopList, noCoopList);

    String perEvaluate = merchantInfo.getPerEvaluate();
    Radar info4 = new Radar(merId, "绩效评估", 10);
    score += this.getSingleScore(perEvaluate, info4, coopList, noCoopList);

    String valueOutSource = merchantInfo.getValuaOutsourc();
    Radar info5 = new Radar(merId, "估值外包", 5);
    score += this.getSingleScore(valueOutSource, info5, coopList, noCoopList);

    String bankMemo = merchantInfo.getBankMemo();
    Radar info6 = new Radar(merId, "银行间备忘录", 10);
    score += this.getSingleScore(bankMemo, info6, coopList, noCoopList);

    String electronicOrder = merchantInfo.getElectrOrder();
    Radar info7 = new Radar(merId, "电子指令直连", 15);
    score += this.getSingleScore(electronicOrder, info7, coopList, noCoopList);

    String fileTransfer = merchantInfo.getFileTransfer();
    Radar info8 = new Radar(merId, "文件传输直连", 6);
    score += this.getSingleScore(fileTransfer, info8, coopList, noCoopList);

    String electronicCheck = merchantInfo.getElectrCheck();
    Radar info9 = new Radar(merId, "电子对账", 9);
    score += this.getSingleScore(electronicCheck, info9, coopList, noCoopList);

    String vitality = merchantInfo.getVitality();
    Radar info10 = new Radar(merId, "参与活跃度", 10);
    score += this.getSingleScore(vitality, info10, coopList, noCoopList);

    return score;
  }

  /**
   * 获取单项值得分
   *
   * @param value      单项值
   * @param radar      计算规则
   * @param coopList   合作业务
   * @param noCoopList 非合作业务
   * @return 单项值得分
   */
  private int getSingleScore(String value, Radar radar, List<Radar> coopList, List<Radar> noCoopList) {
    int score = 0;
    if (SandConvert.obj2Boolean(value, false)) {
      score += radar.getRadarScore();
      coopList.add(radar);
    } else {
      noCoopList.add(radar);
    }

    return score;
  }

  private List<Map<String, Object>> evaluationRisk(List<PublicOpinion> pubOpinList,
                                                   List<InvestSupervise> invSupList, List<ArrearsInfo> arrInfoList) {
    List<Map<String, Object>> riskList = Lists.newArrayList();
    int num1 = 0;
    if (!pubOpinList.isEmpty()) {
      pubOpinList.size();
      num1 = pubOpinList.size();
    }
    Map<String, Object> map1 = Maps.newHashMap();
    map1.put("name", "负面舆情");
    map1.put("max", num1);
    map1.put("detail", pubOpinList);
    riskList.add(map1);

    int num2 = 0;
    if (!invSupList.isEmpty()) {
      num2 = invSupList.size();
    }
    Map<String, Object> map2 = Maps.newHashMap();
    map2.put("name", "投资监督提示");
    map2.put("max", num2);
    map2.put("detail", invSupList);
    riskList.add(map2);

    int num3 = 0;
    if (!arrInfoList.isEmpty()) {
      arrInfoList.size();
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

  private int evaluationScore(List<Radar> baseList) {
    BigDecimal totalScore = new BigDecimal("0.00");
    int basic;
    double weight = 0.09d;
    for (Radar radar : baseList) {
      String name = radar.getName();
      basic = radar.getRadarScore();
      if ("合作深度".equals(name)) {
        weight = 0.4d;
      } else if ("业务规模".equals(name)) {
        weight = 0.24d;
      } else if ("业务收入".equals(name)) {
        weight = 0.18d;
      } else if ("产品数量".equals(name)) {
        weight = 0.09d;
      }
      BigDecimal basicD = new BigDecimal(basic);
      BigDecimal weightD = BigDecimal.valueOf(weight);
      BigDecimal multiply = basicD.multiply(weightD);
      totalScore = multiply.add(totalScore);
    }
    return totalScore.setScale(0, BigDecimal.ROUND_DOWN).intValue();
  }

  private Gauge getMarketAdvice(int gaugeScore) {
    Gauge guage = new Gauge();
    String marketAdvice;
    String name;
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
    log.info("客户等级：{}", name);
    guage.setGaugeScore(gaugeScore);
    log.info("综合得分：{}", gaugeScore);
    guage.setMarketAdvice(marketAdvice);
    log.info("营销建议：{}", marketAdvice);
    return guage;
  }

  private List<Radar> getNoCoopList(List<Radar> noCoopList) {
    int size = noCoopList.size();
    if (!noCoopList.isEmpty()) {
      Collections.sort(noCoopList);
      if (size < 3) {
        return noCoopList;
      } else {
        List<Radar> subList = Lists.newArrayList();
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
  private class Gauge {
    private String name;
    private int gaugeScore;
    private String marketAdvice;
    private String riskAdvice;
    private List<Radar> list;
  }
}
