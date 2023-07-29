package com.atguigu.category.service.impl;

import com.atguigu.category.mapper.CategoryMapper;
import com.atguigu.category.service.CategoryService;
import com.atguigu.clients.ProductClient;
import com.atguigu.param.PageParam;
import com.atguigu.param.ProductHotParam;
import com.atguigu.pojo.Category;
import com.atguigu.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * projectName: b2c-store
 * <p>
 * description: 类别的服务类型
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductClient productClient;

    /**
     * 根据类别名称,查询类别对象
     *
     * @param categoryName
     * @return
     */
    @Override
    public R byName(String categoryName) {

        //封装查询参数
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("category_name",categoryName);
        //查询数据库
        Category category = categoryMapper.selectOne(categoryQueryWrapper);
        //结果封装
        if (category == null){
            log.info("CategoryServiceImpl.byName业务结束，结果:类别查询失败");
            return R.fail("类别查询失败!");
        }
        log.info("CategoryServiceImpl.byName业务结束，结果:{}","类别查询成功");
        return R.ok("类别查询成功!",category);
    }


    /**
     * 根据传入的热门类别名称集合!返回类别对应的id集合
     *
     * @param productHotParam
     * @return
     */
    @Override
    public R hotsCategory(ProductHotParam productHotParam) {

        //封装查询参数
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_name",productHotParam.getCategoryName());
        queryWrapper.select("category_id");

        //查询数据库
        List<Object> ids = categoryMapper.selectObjs(queryWrapper);

        R ok = R.ok("类别集合查询成功", ids);
        log.info("CategoryServiceImpl.hotsCategory业务结束，结果:{}",ok);
        return ok;
    }

    /**
     * 查询类别数据,进行返回!
     *
     * @return r 类别数据集合
     */
    @Override
    public R list() {

        List<Category> categoryList = categoryMapper.selectList(null);
        R ok = R.ok("类别全部数据查询成功!", categoryList);
        log.info("CategoryServiceImpl.list业务结束，结果:{}",ok);
        return ok;
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public R listPage(PageParam pageParam) {

        IPage<Category> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());

        page = categoryMapper.selectPage(page,null);

        return R.ok("类别分页数据查询成功!",page.getRecords(),page.getTotal());
    }

    /**
     * 添加类别信息
     *
     * @param category
     * @return
     */
    @Override
    public R adminSave(Category category) {

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name",category.getCategoryName());
        Long count = categoryMapper.selectCount(queryWrapper);

        if (count > 0){
            return R.fail("类别已经存在,添加失败!");
        }

        int insert = categoryMapper.insert(category);

        log.info("CategoryServiceImpl.adminSave业务结束，结果:{}",insert);

        return R.ok("类别添加成功!");
    }

    /**
     * 删除数据
     *
     * @param categoryId
     * @return
     */
    @Override
    public R adminRemove(Integer categoryId) {

        Long aLong = productClient.adminCount(categoryId);

        if (aLong >0){
            return R.fail("类别删除失败,有:"+aLong+" 件商品正在引用!");
        }

        int i = categoryMapper.deleteById(categoryId);
        log.info("CategoryServiceImpl.adminRemove业务结束，结果:{}",i);
        return R.ok("类别数据删除成功!");
    }

    /**
     * 类别修功能
     *
     * @param category
     * @return
     */
    @Override
    public R adminUpdate(Category category) {

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name",category.getCategoryName());
        Long count = categoryMapper.selectCount(queryWrapper);

        if (count > 0){
            return R.fail("类别已经存在,修改失败!");
        }

        int i = categoryMapper.updateById(category);
        log.info("CategoryServiceImpl.adminUpdate业务结束，结果:{}",i);
        return R.ok("类别数据修改成功!");
    }
}
