# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table chart (
  id                            bigint not null,
  song                          varchar(255),
  artist                        varchar(255),
  release                       varchar(255),
  constraint pk_chart primary key (id)
);
create sequence chart_seq;

drop table if exists chart;
drop sequence if exists chart_seq;