-- 무한매수 계좌
CREATE TABLE aventador.infinite_account (
	account_id int4 DEFAULT nextval('member_account_account_id_seq'::regclass) NOT NULL,
	member_id int4 NULL,
	account_alias varchar NULL,
	account_order int4 NULL,
	registered_date timestamp(0) NULL,
	is_deleted bool DEFAULT false NULL,
	seed numeric(17, 8) NULL,
	fees_per numeric(17, 8) NULL,
	updated_date timestamp NULL,
	kskyj_update_date timestamp NULL,
	CONSTRAINT infinite_account_pk PRIMARY KEY (account_id),
	CONSTRAINT member_account_fk FOREIGN KEY (member_id) REFERENCES aventador.member_info(member_id) ON DELETE CASCADE
);

-- 무한매수 종목
CREATE TABLE aventador.infinite_stock (
	infinite_id serial4 NOT NULL,
	account_id int4 NULL,
	symbol varchar NULL,
	seed numeric(17, 8) NULL,
	infinite_type varchar NULL,
	started_date date NULL,
	infinite_state varchar NULL,
	registered_date timestamp(0) NULL,
	is_deleted bool DEFAULT false NULL,
	infinite_version varchar NULL,
	done_date timestamp NULL,
	kskyj_seed numeric(17, 8) NULL,
	kskyj_average_price numeric(17, 8) NULL,
	kskyj_buy_price numeric(17, 8) NULL,
	kskyj_holding_quantity int4 NULL,
	kskyj_update_date timestamp NULL,
	is_kskyj bool DEFAULT false NULL,
	is_auto_trade bool DEFAULT true NULL,
	divisions int4 NULL,
	CONSTRAINT infinite_stock_pk PRIMARY KEY (infinite_id),
	CONSTRAINT infinite_stock_fk FOREIGN KEY (account_id) REFERENCES aventador.infinite_account(account_id) ON DELETE CASCADE
);

-- 무한매수 매매내역
CREATE TABLE aventador.infinite_history (
	infinite_history_id serial4 NOT NULL,
	infinite_id int4 NULL,
	trade_date date NULL,
	trade_type varchar NULL,
	unit_price numeric(17, 8) NULL,
	quantity int4 NULL,
	registered_type varchar NULL,
	registered_date timestamp(0) NULL,
	updated_date timestamp(0) NULL,
	is_deleted bool DEFAULT false NULL,
	CONSTRAINT infinite_history_pk PRIMARY KEY (infinite_history_id),
	CONSTRAINT infinite_history_fk FOREIGN KEY (infinite_id) REFERENCES aventador.infinite_stock(infinite_id) ON DELETE CASCADE
);

-- 무한매수 손익현황
CREATE TABLE aventador.infinite_income (
	income_id serial4 NOT NULL,
	account_id int4 NULL,
	infinite_id int4 NULL,
	infinite_history_id int4 NULL,
	sell_date date NULL,
	progress_per numeric(17, 8) NULL,
	average_price numeric(17, 8) NULL,
	buy_price numeric(17, 8) NULL,
	sell_price numeric(17, 8) NULL,
	income numeric(17, 8) NULL,
	fees numeric(17, 8) NULL,
	registered_date timestamp NULL,
	is_deleted bool NULL,
	CONSTRAINT infinite_income_pk PRIMARY KEY (income_id),
	CONSTRAINT infinite_income_fk FOREIGN KEY (account_id) REFERENCES aventador.infinite_account(account_id) ON DELETE CASCADE,
	CONSTRAINT infinite_income_fk_1 FOREIGN KEY (infinite_id) REFERENCES aventador.infinite_stock(infinite_id) ON DELETE CASCADE,
	CONSTRAINT infinite_income_fk_2 FOREIGN KEY (infinite_history_id) REFERENCES aventador.infinite_history(infinite_history_id) ON DELETE CASCADE
);

-- 회원정보
CREATE TABLE aventador.member_info (
	member_id serial4 NOT NULL,
	member_email text NULL,
	member_name text NULL,
	member_status text NULL,
	member_roles text NULL,
	subscription_date timestamp NULL,
	unsubscription_date timestamp NULL,
	etc_info json NULL,
	last_login_date timestamp NULL,
	CONSTRAINT member_info_pk PRIMARY KEY (member_id)
);

-- 회원 설정 정보
CREATE TABLE aventador.member_setting (
	member_id int4 NOT NULL,
	setting_detail json NULL,
	api_key varchar NULL,
	fcm_token varchar NULL,
	check_token varchar NULL,
	check_token_available timestamp NULL,
	CONSTRAINT member_setting_pk PRIMARY KEY (member_id),
	CONSTRAINT member_setting_fk FOREIGN KEY (member_id) REFERENCES aventador.member_info(member_id) ON DELETE CASCADE
);

-- 회원 소셜 정보
CREATE TABLE aventador.social_authentication (
	id serial4 NOT NULL,
	member_id int4 NULL,
	social_type text NULL,
	social_id text NULL,
	social_info json NULL,
	CONSTRAINT social_authentication_pk PRIMARY KEY (id),
	CONSTRAINT social_authentication_fk FOREIGN KEY (member_id) REFERENCES aventador.member_info(member_id) ON DELETE CASCADE
);

-- 종목 일봉 정보
CREATE TABLE aventador.stock_history (
	symbol varchar NOT NULL,
	stock_date date NOT NULL,
	price_high numeric(17, 8) NULL,
	price_low numeric(17, 8) NULL,
	price_close numeric(17, 8) NULL,
	price_open numeric(17, 8) NULL,
	prev_close numeric(17, 8) NULL,
	chg numeric(17, 8) NULL,
	chgp numeric(17, 8) NULL,
	volume int8 NULL,
	up_avg numeric(17, 8) NULL,
	dw_avg numeric(17, 8) NULL,
	rsi numeric(17, 8) NULL,
	update_time timestamp NULL,
	CONSTRAINT stock_history_pk PRIMARY KEY (symbol, stock_date)
);


-- 서버 상태 관련
CREATE TABLE aventador.server_status (
	status_code text NOT NULL,
	status_value text NULL,
	status_description text NULL,
	CONSTRAINT server_status_pk PRIMARY KEY (status_code)
);


------------ VIEW

-- 종목 오늘자 정보
CREATE OR REPLACE VIEW aventador.view_today_stock
AS SELECT stock_history.symbol,
    stock_history.stock_date,
    stock_history.price_high,
    stock_history.price_low,
    stock_history.price_close,
    stock_history.price_open,
    stock_history.prev_close,
    stock_history.chg,
    stock_history.chgp,
    stock_history.volume,
    stock_history.up_avg,
    stock_history.dw_avg,
    stock_history.rsi,
    stock_history.update_time
   FROM stock_history
  WHERE ((stock_history.symbol::text, stock_history.stock_date) IN ( SELECT stock_history_1.symbol,
            max(stock_history_1.stock_date) AS stock_date
           FROM stock_history stock_history_1
          GROUP BY stock_history_1.symbol))
  ORDER BY stock_history.stock_date DESC;

-- 무한매수 리스트
CREATE OR REPLACE VIEW aventador.view_infinite_list
AS SELECT mit.member_id,
    iat.account_id,
    ist.infinite_id,
    ist.symbol,
    ist.seed,
    ist.divisions,
    iat.fees_per,
    ist.infinite_type,
    ist.infinite_state,
    ist.infinite_version,
    ist.started_date,
    ist.registered_date,
    ist.done_date,
    ist.is_auto_trade,
    ist.kskyj_seed,
    ist.kskyj_average_price,
    ist.kskyj_buy_price,
    ist.kskyj_holding_quantity,
    ist.kskyj_update_date,
    ist.is_kskyj,
    sum(iht.quantity *
        CASE iht.trade_type
            WHEN '매수'::text THEN 1
            WHEN '매도'::text THEN '-1'::integer
            ELSE NULL::integer
        END)::integer AS holding_quantity,
    sum(iht.unit_price * iht.quantity::numeric) FILTER (WHERE iht.trade_type::text = '매수'::text)::numeric(17,8) AS total_buy_price,
    sum(iht.unit_price * iht.quantity::numeric) FILTER (WHERE iht.trade_type::text = '매도'::text)::numeric(17,8) AS total_sell_price
   FROM member_info mit
     JOIN infinite_account iat ON mit.member_id = iat.member_id
     JOIN infinite_stock ist ON iat.account_id = ist.account_id
     JOIN infinite_history iht ON ist.infinite_id = iht.infinite_id
  WHERE mit.member_status = 'ACTIVE'::text AND iat.is_deleted = false AND ist.is_deleted = false AND iht.is_deleted = false
  GROUP BY mit.member_id, iat.account_id, iat.fees_per, ist.infinite_id, ist.symbol, ist.seed, ist.divisions, ist.infinite_type, ist.infinite_state, ist.started_date, ist.done_date, ist.is_auto_trade, ist.kskyj_seed, ist.kskyj_average_price, ist.kskyj_buy_price, ist.kskyj_holding_quantity, ist.kskyj_update_date, ist.is_kskyj;


-- 무한매수 손익현황 
CREATE OR REPLACE VIEW aventador.view_infinite_income
AS SELECT mit.member_id,
    ist.symbol,
    ist.started_date,
    ist.seed,
    ii.income_id,
    ii.account_id,
    ii.infinite_id,
    ii.infinite_history_id,
    ii.sell_date,
    ii.progress_per,
    ii.average_price,
    ii.buy_price,
    ii.sell_price,
    ii.income,
    ii.fees,
    ii.registered_date,
    ii.is_deleted
   FROM infinite_income ii
     LEFT JOIN infinite_stock ist ON ii.infinite_id = ist.infinite_id
     LEFT JOIN infinite_account iat ON ii.account_id = iat.account_id
     LEFT JOIN member_info mit ON iat.member_id = mit.member_id
  WHERE mit.member_status = 'ACTIVE'::text AND iat.is_deleted = false AND ist.is_deleted = false AND ii.is_deleted = false;

-- 일별 매수 현황
CREATE OR REPLACE VIEW aventador.view_infinite_buy_daily
AS SELECT mit.member_id,
    iat.account_id,
    ist.infinite_id,
    ist.symbol,
    ist.seed,
    ist.kskyj_seed,
    iht.trade_date,
    (COALESCE(sum(iht.unit_price * iht.quantity::numeric) FILTER (WHERE iht.trade_type::text = '매수'::text), 0::numeric) - COALESCE(sum(iht.unit_price * iht.quantity::numeric) FILTER (WHERE iht.trade_type::text = '매도'::text), 0::numeric))::numeric(17,8) AS trade_price
   FROM member_info mit
     JOIN infinite_account iat ON mit.member_id = iat.member_id
     JOIN infinite_stock ist ON iat.account_id = ist.account_id
     JOIN infinite_history iht ON ist.infinite_id = iht.infinite_id
  WHERE mit.member_status = 'ACTIVE'::text AND iat.is_deleted = false AND ist.is_deleted = false AND iht.is_deleted = false AND ist.started_date >= date_trunc('day'::text, CURRENT_DATE - '6 mons'::interval)::date
  GROUP BY mit.member_id, iat.account_id, ist.infinite_id, ist.symbol, ist.seed, ist.kskyj_seed, iht.trade_date;


-- 매수 두달 추이
CREATE OR REPLACE VIEW aventador.view_infinite_buy_two_month
AS SELECT mit.member_id,
    iat.account_id,
    iht.trade_date,
    sum(iht.unit_price * iht.quantity::numeric) FILTER (WHERE iht.trade_type::text = '매수'::text)::numeric(17,8) AS buy_price
   FROM member_info mit
     JOIN infinite_account iat ON mit.member_id = iat.member_id
     JOIN infinite_stock ist ON iat.account_id = ist.account_id
     JOIN infinite_history iht ON ist.infinite_id = iht.infinite_id
  WHERE mit.member_status = 'ACTIVE'::text AND iat.is_deleted = false AND ist.is_deleted = false AND iht.is_deleted = false AND iht.trade_date >= date_trunc('month'::text, CURRENT_DATE - '1 mon'::interval)::date AND iht.trade_date <= date_trunc('month'::text, CURRENT_DATE + '1 mon'::interval)::date
  GROUP BY mit.member_id, iat.account_id, iht.trade_date;