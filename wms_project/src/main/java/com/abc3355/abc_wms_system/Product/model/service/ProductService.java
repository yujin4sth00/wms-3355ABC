package com.abc3355.abc_wms_system.Product.model.service;

import com.abc3355.abc_wms_system.Product.model.dao.InventoryMapperPP;
import com.abc3355.abc_wms_system.Product.model.dao.ProductMapper;
import com.abc3355.abc_wms_system.Product.model.dao.StoreStatusMapper;
import com.abc3355.abc_wms_system.Product.model.dto.ProductSaveReqDto;
import com.abc3355.abc_wms_system.Product.model.dto.ProductUpdateReqDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import static com.abc3355.abc_wms_system.common.MyBatisTemplate.getSqlSession;

public class ProductService {

    /**
     * 신상품 등록
     */
    public int saveProduct(ProductSaveReqDto productSaveReqDto) {
        SqlSession sqlSession = getSqlSession();

        ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
        InventoryMapperPP inventoryMapperPP = sqlSession.getMapper(InventoryMapperPP.class);
        StoreStatusMapper storeStatusMapper = sqlSession.getMapper(StoreStatusMapper.class);

        try{
            // 상품 저장
            int result = productMapper.saveProduct(productSaveReqDto);

            // 재고 저장
            inventoryMapperPP.saveInventoryByNewProduct(productSaveReqDto);

            // 창고 입고 기록 저장
            storeStatusMapper.saveRecordByNewProduct(productSaveReqDto);

            sqlSession.commit();
            return result;
        } catch (Exception e){
            sqlSession.rollback();
            throw new RuntimeException(e);
        } finally {
            sqlSession.close();
        }

    }

    /**
     * 기존 상품 수정
     */
    public int updateProduct(int productNo, ProductUpdateReqDto productUpdateReqDto) {
        SqlSession sqlSession = getSqlSession();

        ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
        productUpdateReqDto.setProductNo(productNo);

        try{
            int result = productMapper.updateProduct(productUpdateReqDto);
            sqlSession.commit();
            return result;
        } catch (Exception e){
            sqlSession.rollback();
            throw new RuntimeException(e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 기존 상품 삭제
     */
    public int deleteProduct(int productNo){

        return 1;
    }


    /**
     * test
     * @return
     */
    public int selectAll() {
        SqlSession sqlSession = getSqlSession();

        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);

        int i = mapper.selectAll();

        return i;
    }


}
