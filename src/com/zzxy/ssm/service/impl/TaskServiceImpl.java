package com.zzxy.ssm.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zzxy.ssm.mapper.AcmgTTaskInstanceMapper;
import com.zzxy.ssm.mapper.AcmgTTaskMapper;
import com.zzxy.ssm.mapper.AcmgTTaskTacheMapper;
import com.zzxy.ssm.po.AcmgTTask;
import com.zzxy.ssm.po.AcmgTTaskCustom;
import com.zzxy.ssm.po.AcmgTTaskInstance;
import com.zzxy.ssm.po.AcmgTTaskInstanceCustom;
import com.zzxy.ssm.po.AcmgTTaskInstanceQueryVO;
import com.zzxy.ssm.po.AcmgTTaskQueryVO;
import com.zzxy.ssm.po.AcmgTTaskTache;
import com.zzxy.ssm.po.AcmgTTaskTacheCustom;
import com.zzxy.ssm.po.AcmgTTaskTacheQueryVO;
import com.zzxy.ssm.service.TaskService;
/**
 * 批量任务调度Service实现类
 * 
 * @工程： 批量调度平台
 * @模块： 
 * 
 * @作者： 王文博
 * @创建日期： 2017年9月13日
 * 
 * @修改记录（修改时间、作者、原因）：
 */
public class TaskServiceImpl implements TaskService {

  @Autowired
  private AcmgTTaskMapper taskMapper;
  
  @Autowired
  private AcmgTTaskTacheMapper tacheMapper;
  
  @Autowired
  private AcmgTTaskInstanceMapper instanceMapper;
  
  /**
   * 保存批量任务调度信息
   */
  @Override
  public void saveTask(AcmgTTask acmgTTask) throws Exception{
    if(StringUtils.isBlank(acmgTTask.getTaskId())) {
      taskMapper.insertTask(acmgTTask);
    }else {
      taskMapper.updateTask(acmgTTask);
    }
    
  }
  
  /**
   * 获取任务调度列表
   */
  @Override
  public List<AcmgTTaskCustom> listTaskCustomByVO(AcmgTTaskQueryVO taskQueryVO) throws Exception {
    return taskMapper.listTaskCustomByVO(taskQueryVO);
  }

  /**
   * 根据ID获取任务信息
   */
  @Override
  public AcmgTTask getTaskById(String taskId) throws Exception{
    return taskMapper.getTaskByPrimaryKey(taskId);
  }

  /**
   * 根据任务标识获取任务信息，用于判断是否存在相同的任务标识
   */
  @Override
  public AcmgTTask getTaskByFlag(String taskFlag) throws Exception{
    AcmgTTaskCustom acmgTTaskCustom = new AcmgTTaskCustom();
    acmgTTaskCustom.setTaskFlag(taskFlag);
    AcmgTTaskQueryVO taskQueryVO = new AcmgTTaskQueryVO(null, acmgTTaskCustom );
    return taskMapper.getTaskByVO(taskQueryVO );
  }

  /**
   * 根据任务编号，查询环节列表
   */
  @Override
  public List<AcmgTTaskTacheCustom> listTacheCustomByTaskId(String taskId) throws Exception {
    AcmgTTaskTacheCustom taskTacheCustom = new AcmgTTaskTacheCustom();
    taskTacheCustom.setTaskId(taskId);
    AcmgTTaskTacheQueryVO queryVO = new AcmgTTaskTacheQueryVO(null, taskTacheCustom );
    return tacheMapper.listTacheCustomByVO(queryVO);
  }

  /**
   * 根据tacheId获取环节信息
   */
  @Override
  public AcmgTTaskTacheCustom getTacheByTacheId(String tacheId) {
    AcmgTTaskTacheCustom taskTacheCustom = new AcmgTTaskTacheCustom();
    taskTacheCustom.setTacheId(tacheId);
    AcmgTTaskTacheQueryVO queryVO = new AcmgTTaskTacheQueryVO(null, taskTacheCustom );
    return tacheMapper.getTacheByVO(queryVO);
  }

  /**
   * 保存环节信息
   * @throws Exception 
   */
  @Override
  public void saveTache(AcmgTTaskTache taskTache) throws Exception {
    if(StringUtils.isBlank(taskTache.getTacheId())) {
      int maxIndex = tacheMapper.getMaxIndexByTaskId(taskTache.getTaskId());
      taskTache.setTacheIndex(maxIndex+1);
      tacheMapper.insertTaskTache(taskTache);
    }else {
      tacheMapper.updateTaskTache(taskTache);
    }
  }

  /**
   * 根据ID删除环节
   */
  @Override
  public void deleteTacheByTacheId(String tacheId) throws Exception{
    tacheMapper.deleteTache(tacheId);
  }

  /**
   * 根据实例ID获取实例信息
   */
  @Override
  public AcmgTTaskInstanceCustom getInstanceByInstanceId(String instanceId) {
    AcmgTTaskInstanceCustom instanceCustom = new AcmgTTaskInstanceCustom();
    instanceCustom.setInstanceId(instanceId);
    AcmgTTaskInstanceQueryVO queryVO = new AcmgTTaskInstanceQueryVO(null, instanceCustom );
    return instanceMapper.getInstanceByVO(queryVO);
  }

  /**
   * 保存实例信息
   */
  @Override
  public void saveInstance(AcmgTTaskInstance instance) {
    if(StringUtils.isBlank(instance.getInstanceId())) {
      instance.setInstanceTime(new Date());
      instanceMapper.insertInstance(instance);
    }else {
      instanceMapper.updateInstance(instance);
    }
  }

  /**
   * 根据环节ID获取实例列表
   */
  @Override
  public List<AcmgTTaskInstanceCustom> listInstanceByTacheId(String tacheId) {
    AcmgTTaskInstanceCustom instanceCustom = new AcmgTTaskInstanceCustom();
    instanceCustom.setTacheId(tacheId);
    AcmgTTaskInstanceQueryVO queryVO = new AcmgTTaskInstanceQueryVO(null, instanceCustom );
    return instanceMapper.listInstanceByVO(queryVO);
  }

  /**
   * 根据任务编号，查询环节列表总数
   */
  @Override
  public int totalTacheByTaskId(String taskId) throws Exception {
    AcmgTTaskTacheCustom taskTacheCustom = new AcmgTTaskTacheCustom();
    taskTacheCustom.setTaskId(taskId);
    AcmgTTaskTacheQueryVO queryVO = new AcmgTTaskTacheQueryVO(null, taskTacheCustom );
    return tacheMapper.countTacheByVO(queryVO);
  }

  /**
   * 根据任务编号，查询环节列表完成数
   */
  @Override
  public int countTacheByTaskIdAndStutas(String taskId,int tacheStutas) throws Exception {
    AcmgTTaskTacheCustom taskTacheCustom = new AcmgTTaskTacheCustom();
    taskTacheCustom.setTaskId(taskId);
    taskTacheCustom.setTacheStutas(tacheStutas);
    AcmgTTaskTacheQueryVO queryVO = new AcmgTTaskTacheQueryVO(null, taskTacheCustom );
    return tacheMapper.countTacheByVO(queryVO);
  }

}
