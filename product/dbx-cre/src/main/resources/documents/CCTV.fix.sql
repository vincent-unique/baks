--CCTV老媒资系统迁CRE，必要的数据转义

--请按顺序执行，切勿修改执行顺序
SET autocommit = 0;
START TRANSACTION;

UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 3 WHERE APPTYPE = 4;
UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 4 WHERE APPTYPE = 8;
-- *** 源库 10 任务分发 对应 任务分配5 ？
UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 5 WHERE APPTYPE = 10;

UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 6 WHERE APPTYPE = 20;
UPDATE MSM_CATALOGINTERFACE SET APPTYPE = 7 WHERE APPTYPE = 40;

COMMIT ;