package com.huntkey.rx.sceo.form.client.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.GenerateStorageClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by wangn1 on 2017/5/26.
 */
@Configuration
@Import(FdfsClientConfig.class)
public class ComponetImport {

}
