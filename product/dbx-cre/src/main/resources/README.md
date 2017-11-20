
*<h3>DBX Simple Specification</h3>*
<span name="Manual Information"/>
<pre><code>
    You may open this file in Markdown Viewer.
</code></pre>

1. 配置数据库连接信息（文件）
2. dbx.war和 startup 文件置于同目录，双击 startup 文件启动web应用
3. 默认：访问 [host / ip]:8086/dbx/ ，即可进入应用首页
4. 若未提前配置连接信息，可在页面初始化连接配置； 点击按钮创建连接
5. 数据表移植页面初始化为当前需求默认要迁移的列表
6. 数据表移植页面提供检索，方便从源库中选择目标进行数据迁移

<pre> <code> 
    # Notice：
    DB-X 数据移植方式抽象了数据移植过程中的通用逻辑，建立了缺省配置情况下的默认移植体系；
    但给予用户最大化配置权限（自定义类型转义、表映射、字段映射、数据修复等等），相关配置以JSON、YAML、Properties 等文件格式存在，在进行修改相关配置信息的之前，
    猜想您对这些文本格式已经较为熟悉。
</code> </pre>


   