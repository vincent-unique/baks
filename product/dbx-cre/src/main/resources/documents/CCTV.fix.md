###CCTV数据移植·数据转义及修复

####1、CCTV_COLUMN_INFO (UPM_COLUMN)
```
    AFD在CRE中无法对应AFDTYPE ,建库脚本中新增了 AFD字段做硬同步，赞不做处理
```

####2、DDM_CLASS (DDM_CLASS)
```
    两个库中TYPE都未给出明确定义，暂不处理
```

####3、DDM_LIMITEDWORD (DDM_LIMITEDWORD)
```
    两个库中TYPE定义一致，不需要转义处理
```
####4、EXC_EXCHANGEATTRIBUTE (EXC_EXCHANGEATTRIBUTE)
```
    两个库中TYPE定义一致，不需要转义处理
```

####5、MSM_ATTRIBUTE（MSM_ATTRIBUTE）
```
    老媒资中的TYPE定义被CRE包含，暂不需要处理
```
####6、MSM_CATALOGCLASS (MSM_CATALOGCLASS)
```
    两库中TYPE定义一致，不需要转义处理
```
####7、MSM_CATALOGINTERFACE (MSM_CATALOGINTERFACE)
```
APPTYPE 需要做数据转义：

    UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 3 WHERE APPTYPE = 4;
    UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 4 WHERE APPTYPE = 8;
    
    *** 源库 10任务分发 未在CRE数据库定义中适配到合适定义，暂映射为特殊值1001
    UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 1001 WHERE APPTYPE = 10;
    
    UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 6 WHERE APPTYPE = 20;
    UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 7 WHERE APPTYPE = 40;

```
####8、MSM_CCCONTENT (MSM_CCCONTENT)
```
    两库中TYPE定义一致，不需要转义处理
```
####9、QMT_WORKFOLDER (QMT_RESOURCELINK)
```
    TYPE在老媒资中无定义，统一采用CRE新库中默认值 0
```

####10、SCP_STREAMMEDIAINFO (SCP_STREAMMEDIAINFO)
```
    两库中FILECLASS定义一致，不需要做转义处理
```

####11、SPM_FILETYPE (SPM_FILETYPE)
```
    老媒资中FILECLASS定义 被CRE包含，暂不需处理
    老媒资中STREAMTYPE定义 被CRE包含，暂不需处理
    两库中VIDEOTYPE定义一致，不需要转义处理
    两库中AUDIOTYPE定义一致，不需要转义处理
    两库中STATUS 定义一致，不需要转义处理
```
####12、SPM_STORAGEAREA (SPM_STORAGEAREA)
```
    两库中TYPE 定义一致，不需要转义处理
    两库中STATUS 定义一致，不需要转义处理

```

####13、UCC_MYWORKFOLDER_CATEGORY (UCP_FOLDER)
```
    TYPE值在老媒资库中无定义，统一采用CRE新库中默认值 6
```