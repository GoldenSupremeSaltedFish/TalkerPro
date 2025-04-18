# TalkerPro 项目 README

## 项目概述

TalkerPro 是一个基于微服务架构的智能对话系统，集成了星火大模型(Spark AI)的API服务。项目包含多个模块，实现了从消息接收、AI处理到结果返回的完整流程。

## 项目结构

```
TalkerPro/
├── GetWay/            # 网关服务
├── PayOff/            # 支付结算服务
├── StarkSparkService/ # 星火大模型集成服务
├── Talker/            # 核心对话服务
```

## 当前项目进度

### 1. StarkSparkService (星火大模型集成服务)

已完成核心功能：
- 实现了与星火大模型的WebSocket连接
- 消息处理流程：
  - 从Kafka接收消息
  - 调用星火API获取响应
  - 处理响应并更新数据库
  - 将结果发送回Kafka
- 实现了QPS限流机制(2次/秒)
- 数据库记录token消耗

主要类：
- <mcsymbol name="SparkApiTalkService" filename="SparkApiTalkService.java" path="StarkSparkService/src/main/java/org/example/starksparkservice/StartSpark/SparkApiTalkService.java" startline="32" type="class"> - 核心消息处理服务
- <mcsymbol name="StarkSparkApiService" filename="StarkSparkApiService.java" path="StarkSparkService/src/main/java/org/example/starksparkservice/StartSpark/StarkSparkApiService.java" startline="24" type="class"> - WebSocket连接和消息发送

### 2. Talker (核心对话服务)

已完成功能：
- Kafka消息生产消费基础实现
- 消息存储到数据库
- 简单的HTTP接口

### 3. PayOff (支付结算服务)

当前状态：
- 基础框架搭建完成
- 支付接口占位符已创建

### 4. GetWay (网关服务)

当前状态：
- 项目结构已创建
- 具体功能待实现

## 技术栈

- **后端**: Spring Boot
- **消息队列**: Kafka
- **数据库**: 关系型数据库(具体类型待确认)
- **AI集成**: 星火大模型API
- **限流算法**: 简单QPS限流

## 下一步计划

1. 完善PayOff模块的支付结算功能
2. 实现GetWay网关的统一入口
3. 增强错误处理和重试机制
4. 实现更精细的限流控制(如漏桶算法)
5. 添加监控和日志系统

## 如何运行

1. 确保安装了Java 17+和Maven
2. 配置Kafka和数据库连接
3. 设置星火大模型的API密钥
4. 分别构建和运行各模块:

```bash
cd StarkSparkService
mvn spring-boot:run
```

```bash
cd Talker
mvn spring-boot:run
```

## 贡献指南

欢迎提交Pull Request。请确保:
- 代码风格一致
- 包含必要的测试
- 更新相关文档

## 许可证

MIT License
