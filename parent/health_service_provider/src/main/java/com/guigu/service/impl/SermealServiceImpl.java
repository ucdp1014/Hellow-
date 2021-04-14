package com.guigu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.guigu.dao.SetmealDao;
import com.guigu.service.SetmealService;
import com.health.constant.RedisConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Setmeal;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("setmealService")
@Transactional
public class SermealServiceImpl implements SetmealService {
    //注入dao
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfig   freeMarkerConfig;
        @Value("${out_put_path}")
    private  String outPuPath;      //从属性文件中读取要生成的html静态页面

    @Override
    public void add(Setmeal setmeal, Integer[] checGroupIds) {
        setmealDao.addSetmeal(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealIdAndCheckGroupId(setmealId,checGroupIds);
        String img = setmeal.getImg();
        //将图片名称保存到Redis集合中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);

        //在添加套餐后重新生成静态页面
        generateMobileStaticHtml();
    }
    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml() {
        //在生成静态页面之前需要查询数据
        List<Setmeal> list = setmealDao.findAll();

        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);

        //需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }
    //生成套餐详情静态页面（可能有多个）
    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map map = new HashMap();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList", list);
        generateHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    //生成套餐详情静态页面（可能有多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            HashMap map = new HashMap();
            map.put("setmeal", setmealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }

    //用于生成静态页面
    public  void generateHtml(String teplateName,String htmlPageName,Map map){
        Configuration configuration = freeMarkerConfig.getConfiguration();//获取配置对象
        //构造输出流
        Writer writer=null;
        try {            //构造输出流
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPuPath + "/" + htmlPageName)), "UTF-8"));
            Template template = configuration.getTemplate(teplateName);
                template.process(map,writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);



        return  new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 移动端
     * @return
     * 查询所有套餐
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 移动端
     * @return
     * 根据id查询套餐
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    @Override
    public List<String> findIdInMiddleTable(String setMealId) {
        List<String> checkgroupIds=new ArrayList<>();
        if (setMealId!=null &&!setMealId.equals("")&&!setMealId.equals(null)){
          checkgroupIds  = setmealDao.findCheckGroupIdsBySetMealId(setMealId);
        }
        return checkgroupIds;
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds, String tempImgId) {

        //把Redis中小集合中存的图片名称替换
        if (tempImgId != null && !tempImgId.equals("")) {
            //清理Redis中小集合内的原图片名
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, tempImgId);
            //将更新后的图片名称存入Redis
            String fileName = setmeal.getImg();
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
        }
        //编辑套餐
        setmealDao.editSetmeal(setmeal);
        //获取SetmealId
        Integer setmealId = setmeal.getId();
        //编辑套餐和检查组关联关系
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //先清空关联关系
            setmealDao.deleteSetmealAndCheckgroup(setmealId);
            this.setSetmealIdAndCheckGroupId(setmealId, checkgroupIds);
        }
    }

    @Override
    public void delete(Integer setMealId, String imgId) {
        //删除Redis中小集合中的图片
        if (imgId != null && !imgId.equals("")) {
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, imgId);
        }
        //删除操作
        if (setMealId!=null&&!setMealId.equals("")){
            //先删除关联关系
            setmealDao.deleteSetmealAndCheckgroup(setMealId);
            //根据id删除套餐
            setmealDao.deleteSetmealById(setMealId);
        }
    }

    @Override
    public List<Setmeal> findAllSetmeal() {
         return setmealDao.findAllSetmeal();
    }


    //抽取的新增关联关系的方法
        public  void setSetmealIdAndCheckGroupId(Integer setmealId,Integer[] checkgroupIds){
            if (checkgroupIds!=null &&checkgroupIds.length>0){
                for (Integer checkgroupId:checkgroupIds
                     ) {
                    Map<String,Integer> map=new HashMap();
                    map.put("setmealId",setmealId);
                    map.put("checkgroupId",checkgroupId);
                    setmealDao.setSetmealIdAndCheckGroupId(map);
                }

            }
        }
}
