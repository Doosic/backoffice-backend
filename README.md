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
