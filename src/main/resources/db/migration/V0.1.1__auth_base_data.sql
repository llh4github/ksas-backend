insert into public.auth_user (id, username, password, nickname, created_time, updated_time, updated_by_user_id,
                              created_by_user_id)
values (114514, 'Tom', '$2a$10$zZfKqKt6PE5bhGbOuwez3OXpXLzLYe/67V8WFKj23Vrre72HXUPLm', 'Tom',
        '2024-11-03 11:16:05.000000', '2024-11-03 11:16:09.000000', null, null),
       (624746114797637, 'visitor', '$2a$10$zZfKqKt6PE5bhGbOuwez3OXpXLzLYe/67V8WFKj23Vrre72HXUPLm', '访客',
        '2024-12-20 10:38:28.936841', '2024-12-20 10:53:38.395028', 614558031941701, 614558031941701),
       (614558031941701, 'test', '$2a$10$zZfKqKt6PE5bhGbOuwez3OXpXLzLYe/67V8WFKj23Vrre72HXUPLm', 'qewr',
        '2024-11-21 15:43:04.022721', '2024-12-07 15:04:15.147433', 114514, 114514);

insert into public.auth_role (id, title, code, created_time, updated_time, updated_by_user_id, created_by_user_id,
                              remark)
values (1, '管理员', 'admin', '2024-11-20 15:37:54.151001', '2024-12-07 16:02:13.429746', 114514, 114514,
        '拥有所有权限'),
       (2, '系统维护员', 'maintainer', '2024-11-20 15:37:54.151001', '2024-12-14 21:31:15.572349', 114514, 114514,
        '只能操作系统管理下的页面'),
       (614202868330565, '访客', 'visitors', '2024-11-20 15:37:54.151001', '2024-12-23 09:27:49.394329',
        614558031941701, 114514, '只能看');

INSERT INTO public.auth_permission (id, title, code, remark, created_time, updated_time, updated_by_user_id,
                                    created_by_user_id)
VALUES (114514, '所有权限', '*:*:*', '所有接口都能访问', '2024-12-07 16:01:27.441', '2024-12-07 15:34:20.708', 114514,
        NULL),
       (612000379674693, '更新角色', 'auth:role:update', '', '2024-11-14 10:15:57.209', '2024-12-14 15:43:57.377',
        114514, 114514),
       (614288251818053, '根据ID获取角色', 'auth:role:view:id', '', '2024-11-20 21:25:19.729',
        '2024-12-14 21:23:12.401', 114514, 114514),
       (620218132709445, '查看角色数据', 'auth:role:view:page', '', '2024-12-07 15:34:04.557',
        '2024-12-14 15:44:34.345', 114514, 114514),
       (620219595882565, '新增角色', 'auth:role:add', '', '2024-12-07 15:40:01.776', '2024-12-14 15:43:03.944', 114514,
        114514),
       (620223309852741, '根据ID获取用户', 'auth:user:view:id', '', '2024-12-07 15:55:08.507',
        '2024-12-14 15:41:39.253', 114514, 114514),
       (620223505530949, '修改用户信息', 'auth:user:update', '修改用户', '2024-12-07 15:55:56.280',
        '2024-12-14 15:40:44.789', 114514, 114514),
       (620223681163333, '用户分页查询', 'auth:user:view:page', '用户分页查询', '2024-12-07 15:56:39.159',
        '2024-12-14 15:35:44.005', 114514, 114514);

INSERT INTO public.link_role_permission (role_id, permission_id)
VALUES (1, 114514),
       (2, 612000379674693),
       (2, 614288251818053),
       (2, 620218132709445),
       (2, 620219595882565),
       (2, 620223309852741),
       (2, 620223505530949),
       (2, 620223681163333),
       (614202868330565, 620218132709445),
       (614202868330565, 620223681163333);

