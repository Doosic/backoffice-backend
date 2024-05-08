<h1>backoffice</h1>
<p>backoffice backend project</p>

<h2>Database Table SQL(postgres)</h2>
<p>
CREATE TABLE IF NOT EXISTS bk_admin ( <br/>
&nbsp&nbsp admin_id BIGSERIAL PRIMARY KEY, <br/>
&nbsp&nbsp email VARCHAR(50) NOT NULL, <br/>
&nbsp&nbsp name VARCHAR(50) NOT NULL, <br/>
&nbsp&nbsp password VARCHAR(510) NOT NULL, <br/>
&nbsp&nbsp login_fail_count INT NOT NULL DEFAULT 0, <br/>
&nbsp&nbsp status VARCHAR NOT NULL DEFAULT 'USE', <br/>
&nbsp&nbsp create_date TIMESTAMP, <br/>
&nbsp&nbsp modified_date TIMESTAMP, <br/>
&nbsp&nbsp lock_date TIMESTAMP, <br/>
&nbsp&nbsp delete_date TIMESTAMP <br/>
);
</p>

<p>
CREATE TABLE IF NOT EXISTS bk_menu ( <br/>
&nbsp&nbsp menu_id BIGSERIAL PRIMARY KEY, <br/>
&nbsp&nbsp menu_parent BIGSERIAL NOT NULL, <br/>
&nbsp&nbsp menu_name varchar(60), <br/>
&nbsp&nbsp menu_level int4, <br/>
&nbsp&nbsp menu_order int4, <br/>
&nbsp&nbsp menu_type varchar(30), <br/>
&nbsp&nbsp menu_icon varchar(60), <br/>
&nbsp&nbsp menu_link varchar(60), <br/>
&nbsp&nbsp menu_query varchar(255), <br/>
);
</p>

<p>
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link, menu_query)
values (default, -1, 'HOME', 1, 1, 'ITEM', 'pi pi-user-plus', '/', ''); <br/>
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link, menu_query)
values (default, 1, 'dashboard', 2, 2, 'TREE', 'pi pi-user-plus', '/', '{"search": "", "page": 1, "rows": 10}');<br/>

insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link, menu_query)
values (default, -1, 'ADMIN', 1, 1, 'ITEM', 'pi pi-user-plus', '/admin/list', '{"search": "", "page": 1, "rows": 10}');<br/>
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link, menu_query)
values (default, 2, 'admin-list', 2, 2, 'TREE', 'pi pi-fw pi-id-card', '/admin/list', '{"search": "", "page": 1, "rows": 10}');<br/>
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link, menu_query)
values (default, 2, 'auth-list', 2, 3, 'TREE', 'pi pi-user-plus','/auth/list', '{"search": "", "page": 1, "rows": 10}');<br/>
</p>

<p>
CREATE TABLE IF NOT EXISTS bk_auth ( <br/>
&nbsp&nbsp auth_id BIGSERIAL PRIMARY KEY, <br/>
&nbsp&nbsp auth_name BIGSERIAL NOT NULL, <br/>
&nbsp&nbsp auth_type varchar(60) <br/>
);
</p>

<p>
CREATE TABLE IF NOT EXISTS bk_auth_menu ( <br/>
&nbsp&nbsp auth_menu_id BIGSERIAL PRIMARY KEY, <br/>
&nbsp&nbsp auth_id BIGSERIAL NOT NULL, <br/>
&nbsp&nbsp menu_id BIGSERIAL NOT NULL <br/>
);
</p>

<p>
insert into bk_auth (auth_id, auth_name, auth_type) values (default, 'SUPER', 'MENU'); <br/>
insert into bk_auth_menu (auth_menu_id, auth_id, menu_id) values (default, 1, 1); <br/>
insert into bk_auth_menu (auth_menu_id, auth_id, menu_id) values (default, 1, 2); <br/>
insert into bk_auth_menu (auth_menu_id, auth_id, menu_id) values (default, 1, 3); <br/>
insert into bk_auth_menu (auth_menu_id, auth_id, menu_id) values (default, 1, 4); <br/>
insert into bk_auth_menu (auth_menu_id, auth_id, menu_id) values (default, 1, 5); <br/>

insert into bk_auth (auth_id, auth_name, auth_type) values (default, 'TEAM_BK', 'MENU'); <br/>
insert into bk_auth_menu (auth_menu_id, auth_id, menu_id) values (default, 2, 1); <br/>
insert into bk_auth_menu (auth_menu_id, auth_id, menu_id) values (default, 2, 3); <br/>
</p>

<p>
CREATE TABLE IF NOT EXISTS bk_function ( <br/>
&nbsp&nbsp func_id BIGSERIAL PRIMARY KEY, <br/>
&nbsp&nbsp func_parent BIGSERIAL NOT NULL, <br/>
&nbsp&nbsp func_name VARCHAR(60), <br/>
&nbsp&nbsp func_level int4, <br/>
&nbsp&nbsp func_order int4, <br/>
&nbsp&nbsp menu_type VARCHAR(30), <br/>
&nbsp&nbsp func_icon VARCHAR(60) <br/>
);
</p>

<p>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon) 
values (default, -1, 'dashboard', 1, 1, 'TREE', 'pi pi-fw pi-fw pi-plus-circle');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon) 
values (default, -1, 'admin-list', 1, 1, 'TREE', 'pi pi-fw pi-fw pi-pencil');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon) 
values (default, -1, 'auth-list', 1, 1, 'TREE', 'pi pi-fw pi-trash');<br/>

insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 2, 'adminlist-create', 2, 2, 'ITEM', 'pi pi-fw pi-plus-circle');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 2, 'adminlist-update', 3, 3, 'ITEM', 'pi pi-fw pi-pencil');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 2, 'adminlist-delete', 4, 4, 'ITEM', 'pi pi-fw pi-trash');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 2, 'adminlist-unlock', 5, 5, 'ITEM', 'pi pi pi-fw pi-unlock');<br/>

insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 3, 'authlist-menu-create', 2, 2, 'ITEM', 'pi pi-fw pi-plus-circle');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 3, 'authlist-menu-update', 3, 3, 'ITEM', 'pi pi-fw pi-pencil');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 3, 'authlist-menu-delete', 4, 4, 'ITEM', 'pi pi-fw pi-trash');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 3, 'authlist-func-create', 5, 5, 'ITEM', 'pi pi-fw pi-plus-circle');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 3, 'authlist-func-update', 6, 6, 'ITEM', 'pi pi-fw pi-pencil');<br/>
insert into bk_function (func_id, func_parent, func_name, func_level, func_order, menu_type, func_icon)
values (default, 3, 'authlist-func-delete', 7, 7, 'ITEM', 'pi pi-fw pi-trash');<br/>
</p>

<p>
CREATE TABLE IF NOT EXISTS bk_auth_function ( <br/>
&nbsp&nbsp auth_func_id BIGSERIAL PRIMARY KEY, <br/>
&nbsp&nbsp auth_id BIGSERIAL, <br/>
&nbsp&nbsp func_id BIGSERIAL <br/>
);
</p>

<p>
insert into bk_auth (auth_id, auth_name, auth_type, reg_user) values (default, 'SUPER_ FUNCTION', 'FUNC', 3);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 1);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 2);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 3);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 4);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 5);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 6);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 7);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 8);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 9);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 10);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 11);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 12);<br/>
insert into bk_auth_function (auth_func_id, auth_id, func_id) values (default, 7, 13);<br/>
</p>