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
import com.sand.graphic.entity.DailyDeposit;
import com.sand.graphic.entity.MerchantInfo;
import com.sand.graphic.mapper.DailyDepositMapper;
import com.sand.graphic.mapper.MerchantInfoMapper;
import com.sand.graphic.service.IMerchantInfoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    Map<String, Object> allResult = evaluationMerchant(merchantInfo, depositList);
    // 查询风险画像
    // 计算时间区间
    Date date = getTimeInterval();

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
    double dailyDeposit = 0d;
    for (DailyDeposit info : depositList) {
      dailyDeposit += info.getDailyDeposit();
    }
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
}
