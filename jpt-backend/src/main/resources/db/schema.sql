drop view v_method_statistics if exists;
drop table t_snapshot_row if exists;
drop table t_snapshot if exists;

create table t_snapshot(
  id            varchar(36)  primary key not null,
  description   varchar(500) null,
  uploaded_time TIMESTAMP    null
);

create table t_snapshot_row(
  id           varchar(36) primary key not null,
  snapshot_id  varchar(36) null,
  thread_id    long null,
  hierarchy    long null,
  serial       long null,
  mergered     bool null,
  parent_id    varchar(36)  null,
  package_name varchar(500) null,
  class_name   varchar(500) null,
  method_name  varchar(200) null,
  in_or_out    varchar(5)   null,
  start_time   TIMESTAMP    null,
  end_time     TIMESTAMP    null,
  used_time    long         null
);

create view v_method_statistics as
  select snapshot_id, package_name,class_name,method_name,count(*) invoked_count, min(start_time) first_invoke, max(end_time) last_invoke_end, sum(used_time ) used_time
  from t_snapshot_row 
  where MERGERED =true and in_or_out='in'
  group by snapshot_id, package_name,class_name,method_name
