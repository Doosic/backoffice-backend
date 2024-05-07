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
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link)
values (default, -1, 'HOME', 1, 1, 'ITEM', 'pi pi-user-plus', '/', ''); <br/>
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link)
values (default, 1, 'Dashboard', 2, 2, 'TREE', 'pi pi-user-plus', '/', '');<br/>

insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link)
values (default, -1, 'ADMIN', 1, 1, 'ITEM', 'pi pi-user-plus', '/admin/list', '');<br/>
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link)
values (default, 2, 'AdminList', 2, 2, 'TREE', 'pi pi-user-plus', '/', '');<br/>
insert into bk_menu (menu_id, menu_parent, menu_name, menu_level, menu_order, menu_type, menu_icon, menu_link)
values (default, 2, 'AuthList', 2, 3, 'TREE', 'pi pi-user-plus', '/', '');<br/>
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