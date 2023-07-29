package com.atguigu.product.service.impl;

import com.atguigu.clients.*;
import com.atguigu.param.ProductHotParam;
import com.atguigu.param.ProductIdsParam;
import com.atguigu.param.ProductSaveParam;
import com.atguigu.param.ProductSearchParam;
import com.atguigu.pojo.Category;
import com.atguigu.pojo.Picture;
import com.atguigu.pojo.Product;
import com.atguigu.product.mapper.PictureMapper;
import com.atguigu.product.mapper.ProductMapper;
import com.atguigu.product.service.ProductService;
import com.atguigu.to.OrderToProduct;
import com.atguigu.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.scenario.effect.impl.prism.PrCropPeer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * projectName: b2c-store
 * <p>
 * description: 类别的实现类
 */
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements ProductService {


    //引入feign客户端需要,在启动类添加配置注解
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SearchClient searchClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private CollectClient collectClient;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PictureMapper pictureMapper;

    /**
     * 单类别名称 查询热门商品 至多7条数据
     *    1. 根据类别名称 调用 feign客户端访问类别服务获取类别的数据
     *    2. 成功 继续根据类别id查询商品数据  [热门 销售量倒序 查询7]
     *    3. 结果封装即可
     * @param categoryName 类别名称
     * @return r
     */
    @Cacheable(value = "list.product", key = "#categoryName" , cacheManager = "cacheManagerDay")
    @Override
    public R promo(String categoryName) {

        R r = categoryClient.byName(categoryName);

        if (r.getCode().equals(R.FAIL_CODE)) {
            log.info("ProductServiceImpl.promo业务结束，结果:{}","类别查询失败!");
            return r;
        }

        // 类别服务中 data = category --- feign {json}  ----- product服务 LinkedHashMap jackson

        LinkedHashMap<String,Object>  map = (LinkedHashMap<String, Object>) r.getData();

        Integer categoryId = (Integer) map.get("category_id");

        //封装查询参数
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",categoryId);
        queryWrapper.orderByDesc("product_sales");

        IPage<Product> page = new Page<>(1,7);

        //返回的是包装数据! 内部有对应的商品集合,也有分页的参数 例如: 总条数 总页数等等
        page = productMapper.selectPage(page, queryWrapper);

        List<Product> productList = page.getRecords(); //指定页的数据
        long total = page.getTotal(); //获取总条数

        log.info("ProductServiceImpl.promo业务结束，结果:{}",productList);

        return R.ok("数据查询成功",productList);
    }


    /**
     * 多类别热门商品查询 根据类别名称集合! 至多查询7条!
     *   1. 调用类别服务
     *   2. 类别集合id查询商品
     *   3. 结果集封装即可
     * @param productHotParam 类别名称集合
     * @return r
     */
    @Cacheable(value = "list.product",key = "#productHotParam.categoryName")
    @Override
    public R hots(ProductHotParam productHotParam) {

        R r = categoryClient.hots(productHotParam);

        if(r.getCode().equals(R.FAIL_CODE)){
            log.info("ProductServiceImpl.hots业务结束，结果:{}",r.getMsg());
            return r;
        }

        List<Object> ids = (List<Object>) r.getData();

        //进行商品数据查询
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_id",ids);
        queryWrapper.orderByDesc("product_sales");

        IPage<Product> page = new Page<>(1,7);

        page = productMapper.selectPage(page,queryWrapper);

        List<Product> records = page.getRecords();

        R ok = R.ok("多类别热门商品查询成功!", records);

        log.info("ProductServiceImpl.hots业务结束，结果:{}",ok);

        return ok;
    }


    /**
     * 查询类别商品集合
     *
     * @return
     */
    @Override
    public R clist() {
        R r = categoryClient.list();
        log.info("ProductServiceImpl.clist业务结束，结果:{}",r);

        return r;
    }

    /**
     * 通用性的业务!
     * 传入了类别id,根据id查询并且分页
     * 没有传入类别的id! 查询全部!
     *
     * @param productIdsParam
     * @return
     */
    @Cacheable(value = "list.product",key = "#productIdsParam.categoryID+'-'+#productIdsParam.currentPage+'-'+#productIdsParam.pageSize")
    @Override
    public R byCategory(ProductIdsParam productIdsParam) {

       List<Integer> categoryID = productIdsParam.getCategoryID();

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (!categoryID.isEmpty()) {
            queryWrapper.in("category_id",categoryID);
        }

        IPage<Product> page = new Page<>(productIdsParam.getCurrentPage(),productIdsParam.getPageSize());

        page = productMapper.selectPage(page,queryWrapper);

        //结果集封装
        R ok = R.ok("查询成功!", page.getRecords(), page.getTotal());

        log.info("ProductServiceImpl.byCategory业务结束，结果:{}",ok);

        return ok;
    }

    /**
     * 根据商品id,查询商品详情信息
     *
     * @param productID
     * @return
     */
    @Cacheable(value = "product",key = "#productID")
    @Override
    public R detail(Integer productID) {

        Product product = productMapper.selectById(productID);

        R ok = R.ok(product);
        log.info("ProductServiceImpl.detail业务结束，结果:{}",ok);
        return ok;
    }

    /**
     * 查询商品对应的图片详情集合
     *
     * @param productID
     * @return
     */
    @Cacheable(value="picture",key = "#productID")
    @Override
    public R pictures(Integer productID) {

        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("product_id",productID);

        List<Picture> pictureList = pictureMapper.selectList(queryWrapper);

        R ok = R.ok(pictureList);

        log.info("ProductServiceImpl.pictures业务结束，结果:{}",ok);
        return ok;
    }

    /**
     * 搜索服务调用,获取全部商品数据!
     * 进行同步!
     *
     * @return 商品数据集合
     */
    @Cacheable(value = "list.category",key = "#root.methodName",cacheManager = "cacheManagerDay")
    @Override
    public List<Product> allList() {

        List<Product> productList = productMapper.selectList(null);
        log.info("ProductServiceImpl.allList业务结束，结果:{}",productList.size());
        return productList;
    }

    /**
     * 搜索业务.需要调用搜索服务!
     *
     * @param productSearchParam
     * @return
     */
    @Override
    public R search(ProductSearchParam productSearchParam) {

        R r = searchClient.search(productSearchParam);
        log.info("ProductServiceImpl.search业务结束，结果:{}",r);
        return r;
    }

    /**
     * 根据商品id集合查询商品信息
     *
     * @param productIds
     * @return
     */
    @Cacheable(value = "list.product",key = "#productIds")
    @Override
    public R ids(List<Integer> productIds) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("product_id",productIds);

        List<Product> productList = productMapper.selectList(queryWrapper);

        R r = R.ok("类别信息查询成功!", productList);
        log.info("ProductServiceImpl.ids业务结束，结果:{}",r);
        return r;
    }

    /**
     * 根据商品id,查询商品id集合
     *
     * @param productIds
     * @return
     */
    @Override
    public List<Product> cartList(List<Integer> productIds) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("product_id",productIds);

        List<Product> productList = productMapper.selectList(queryWrapper);
        log.info("ProductServiceImpl.cartList业务结束，结果:{}",productList);
        return productList;
    }


    /**
     * 修改库存和增加销售量
     *
     * @param orderToProducts
     */
    @Override
    public void subNumber(List<OrderToProduct> orderToProducts) {

        //将集合转成map  productId orderToProduct
        Map<Integer, OrderToProduct> map = orderToProducts.stream().collect(Collectors.toMap(OrderToProduct::getProductId, v -> v));
        //获取商品的id集合
        Set<Integer> productIds = map.keySet();
        //查询集合对应的商品信息
        List<Product> productList = productMapper.selectBatchIds(productIds);
        //修改商品信息
        for (Product product : productList) {
            Integer num = map.get(product.getProductId()).getNum();
            product.setProductNum(product.getProductNum() - num); //减库存
            product.setProductSales(product.getProductSales()+num); //添加销售量
        }
        //批量更新
        this.updateBatchById(productList);
        log.info("ProductServiceImpl.subNumber业务结束，结果:库存和销售量的修改完毕");
    }

    /**
     * 类别对应的商品数量查询
     *
     * @param categoryId
     * @return
     */
    @Override
    public Long adminCount(Integer categoryId) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",categoryId);

        Long count = baseMapper.selectCount(queryWrapper);
        log.info("ProductServiceImpl.adminCount业务结束，结果:{}",count);
        return count;
    }


    /**
     * 商品保存业务
     *    1. 商品数据保存
     *    2. 商品的图片详情切割和保存
     *    3. 搜索数据库的数据添加
     *    4. 清空商品相关的缓存数据
     * @param productSaveParam
     * @return
     */
    @CacheEvict(value = "list.product",allEntries = true)
    @Override
    public R adminSave(ProductSaveParam productSaveParam) {

        Product product = new Product();
        BeanUtils.copyProperties(productSaveParam,product);

        int rows = productMapper.insert(product); //商品数据插入
        log.info("ProductServiceImpl.adminSave业务结束，结果:{}",rows);

        //商品图片获取 +
        String pictures = productSaveParam.getPictures();

        if (!StringUtils.isEmpty(pictures)){
            //截取特殊字符串的时候 \\ [] 包含 $ + * | ?
            String[] urls = pictures.split("\\+");
            for (String url : urls) {
                Picture picture = new Picture();
                picture.setProductId(product.getProductId());
                picture.setProductPicture(url);
                pictureMapper.insert(picture); //插入商品的图片
            }
        }

        //同步搜索服务的数据
        searchClient.saveOrUpdate(product);

        return R.ok("商品数据添加成功!");
    }

    /**
     * 商品数据更新
     *    1.更新商品数据
     *    2.同步搜索服务数据即可
     * @param product
     * @return
     */
    @Override
    public R adminUpdate(Product product) {

        productMapper.updateById(product);

        //同步搜索服务的数据
        searchClient.saveOrUpdate(product);

        return R.ok("商品数据更新成功!");
    }

    /**
     * 商品删除业务
     *   1.检查购物车
     *   2.简单订单
     *   3.删除商品
     *   4.删除商品相关的图片
     *   5.删除收藏
     *   6.进行es数据同步
     *   7.清空缓存
     * @param productId
     * @return
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "product.list",allEntries = true),
                    @CacheEvict(value = "product", key = "#productId")
            }
    )
    @Override
    public R adminRemove(Integer productId) {

        R r = cartClient.check(productId);

        if ("004".equals(r.getCode())){
            log.info("ProductServiceImpl.adminRemove业务结束，结果:{}",r.getMsg());
            return r;
        }

        r = orderClient.check(productId);
        if ("004".equals(r.getCode())){
            log.info("ProductServiceImpl.adminRemove业务结束，结果:{}",r.getMsg());
            return r;
        }

        //删除商品
        productMapper.deleteById(productId);
        //删除商品图片
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id",productId);
        pictureMapper.delete(queryWrapper);

        //删除收藏中和本商品有关的!
        collectClient.remove(productId);

        //同步数据
        searchClient.remove(productId);

        return R.ok("商品删除成功!");
    }
}
