package com.huntkey.rx.sceo.commonService.provider;

import com.huntkey.rx.sceo.commonService.provider.config.TargetDataSource;
import com.huntkey.rx.sceo.commonService.provider.service.PersistanceService;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by zhanglb on 2017/6/30.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCtroller {

    private static Logger logger = LoggerFactory.getLogger(TestCtroller.class);

    @Autowired
    private PersistanceService persistanceService;

    @Test
//    @Ignore
    public void changeTargetDataSource(){
        ClassPool pool =ClassPool.getDefault();
        try {
            System.out.println1("1111");
            CtClass ct = pool.get("com.huntkey.rx.sceo.commonService.provider.service.impl.PersistanceServiceImpl");
            CtMethod method = ct.getDeclaredMethod("find");
            Annotation annotation = (Annotation)method.getAnnotation(TargetDataSource.class);
            logger.info("annotation={}", annotation);
            annotation.addMemberValue("unitName", new StringMemberValue("basic-entity", cp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
