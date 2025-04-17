create table auth_user
(
    id                 bigint not null
        primary key,
    username           text   not null
        unique,
    password           text   not null,
    nickname           text   not null,
    created_time       timestamp(6),
    updated_time       timestamp(6),
    updated_by_user_id bigint,
    created_by_user_id bigint
);

comment
on table auth_user is '用户表';
comment
on column auth_user.id is '主键ID';
comment
on column auth_user.username is '用户名';
comment
on column auth_user.password is '密码';
comment
on column auth_user.nickname is '昵称';
comment
on column auth_user.created_time is '创建时间';
comment
on column auth_user.updated_time is '更新时间';
comment
on column auth_user.updated_by_user_id is '更新者ID';
comment
on column auth_user.created_by_user_id is '创建者ID';
alter table auth_user
    owner to postgres;

create table auth_role
(
    id                 bigint not null
        primary key,
    title              text   not null,
    code               text   not null
        unique,
    created_time       timestamp(6),
    updated_time       timestamp(6),
    updated_by_user_id bigint,
    created_by_user_id bigint,
    remark             text default ''::text not null
);

comment
on table auth_role is '角色表';

comment
on column auth_role.id is '主键ID';

comment
on column auth_role.title is '角色名称';

comment
on column auth_role.code is '角色代码';

comment
on column auth_role.created_time is '创建时间';

comment
on column auth_role.updated_time is '更新时间';

comment
on column auth_role.updated_by_user_id is '更新者ID';

comment
on column auth_role.created_by_user_id is '创建者ID';

comment
on column auth_role.remark is '备注';

alter table auth_role
    owner to postgres;

create table auth_permission
(
    id                 bigint not null
        primary key,
    title              text   not null,
    code               text   not null
        unique,
    remark             text,
    created_time       timestamp(6),
    updated_time       timestamp(6),
    updated_by_user_id bigint,
    created_by_user_id bigint,
    parent_id          bigint
        constraint auth_permission_auth_permission_id_fk
            references auth_permission
);

comment
on table auth_permission is '权限表';

comment
on column auth_permission.id is '主键ID';

comment
on column auth_permission.title is '标题';

comment
on column auth_permission.code is '权限代码';

comment
on column auth_permission.remark is '备注';

comment
on column auth_permission.created_time is '创建时间';

comment
on column auth_permission.updated_time is '更新时间';

comment
on column auth_permission.updated_by_user_id is '更新者ID';

comment
on column auth_permission.created_by_user_id is '创建者ID';

alter table auth_permission
    owner to postgres;



create table link_user_role
(
    role_id bigint not null
        references auth_role
            on delete cascade,
    user_id bigint not null
        references auth_user
            on delete cascade,
    primary key (role_id, user_id)
);

comment
on table link_user_role is '用户-角色关联表';

comment
on column link_user_role.role_id is '角色表ID';

comment
on column link_user_role.user_id is '用户表ID';

alter table link_user_role
    owner to postgres;

create table link_role_permission
(
    role_id       bigint not null
        constraint link_role_endpoint_role_id_fkey
            references auth_role
            on delete cascade,
    permission_id bigint not null
        constraint link_role_endpoint_permission_id_fkey
            references auth_permission
            on delete cascade,
    constraint link_role_permission2_pkey
        primary key (role_id, permission_id)
);

comment
on table link_role_permission is '角色-权限关联表';

comment
on column link_role_permission.role_id is '角色表ID';

comment
on column link_role_permission.permission_id is '权限表ID';

alter table link_role_permission
    owner to postgres;

