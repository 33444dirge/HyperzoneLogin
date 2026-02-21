# ML 源库 H2 1.x 到 2.x 转换指南

本文用于解决 `hzl-merge ml` 读取旧版 H2 数据库时报错：

- `90048 Unsupported database file version or invalid file header`

适用场景：旧库是 `*.mv.db`，需要转到新版 H2 才能继续迁移。

## 1. 准备

下载两个版本的 H2 工具包（jar）：

- 导出用：`h2-2.1.214.jar`
- 导入用：`h2-2.3.232.jar`

建议把两个 jar 放在当前工作目录。

旧库示例：

- `D:/test/mixedlogin/vc/plugins/hyperzonelogin/merge/multilogin.mv.db`

注意：H2 URL 中不要写 `.mv.db` 后缀。

## 2. 先校验是否连到正确旧库（强烈建议）

如果这里查不出你的业务表，说明路径不对；继续导出只会得到 `CREATE USER`。

```powershell
java -cp ".\h2-2.1.214.jar" org.h2.tools.Shell -url "jdbc:h2:file:./merge/multilogin;IFEXISTS=TRUE" -user root -password root -sql "SHOW TABLES"
```

## 3. 导出旧库为 SQL

关键点：增加 `IFEXISTS=TRUE`，避免路径写错时自动创建空库。

```powershell
java -cp ".\h2-2.1.214.jar" org.h2.tools.Script -url "jdbc:h2:file:./merge/multilogin;IFEXISTS=TRUE" -user root -password root -script ".\merge\ml_dump.sql"
```

## 4. 导入到新 H2 2.x

```powershell
java -cp ".\h2-2.3.232.jar" org.h2.tools.RunScript -url "jdbc:h2:file:./merge/multilogin_v232;MODE=MySQL" -user sa -password "" -script ".\merge\ml_dump.sql" -options "FROM_1X"
```

## 5. 更新 merge-ml.conf

你可以二选一：

### 方式 A：使用 path

```hocon
source {
  type = "H2DB"
  h2 {
    path = "D:/test/mixedlogin/vc/plugins/hyperzonelogin/merge/multilogin_v2"
    parameters = "MODE=MySQL"
    username = "sa"
    password = ""
  }
}
```

### 方式 B：直接使用 jdbcUrl（推荐排障时使用）

```hocon
source {
  type = "H2DB"
  h2 {
    jdbcUrl = "jdbc:h2:file:D:/test/mixedlogin/vc/plugins/hyperzonelogin/merge/multilogin_v2;MODE=MySQL"
    username = "sa"
    password = ""
  }
}
```

## 6. 执行迁移

在服务器中执行：

```text
/hzl-merge ml
```

迁移日志输出位置：

- `plugins/hyperzonelogin/merge/merge-ml.log`

## 常见问题

### Q1: 报 `90011`（relative path not allowed）

请将 `path` 改为绝对路径，或使用 `jdbcUrl` 明确指定绝对 `file:` URL。

### Q2: 报 `No suitable driver found`

确认你使用的是包含完整依赖的插件构建产物，并且服务端已重启加载最新 jar。

### Q3: 导入后表名不一致

请在 `merge-ml.conf` 里检查：

- `tables.userDataTable`
- `tables.inGameProfileTable`
