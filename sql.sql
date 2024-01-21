PGDMP          4                {            Dream    15.4    15.4 �    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    33501    Dream    DATABASE        CREATE DATABASE "Dream" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Vietnamese_Vietnam.1258';
    DROP DATABASE "Dream";
                postgres    false                       1255    33502    check_daily_discount()    FUNCTION     �  CREATE FUNCTION public.check_daily_discount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF EXTRACT(hour FROM now()) = 7 THEN
        INSERT INTO public.notification (idaccount, title, notification_text, idrole, image)
        SELECT DISTINCT d.idaccount, 
               'Sự kiện giảm giá đang diễn ra',
               'Đang diễn ra sự kiện giảm giá ' || d.discountname || ' đến hết ngày ' || d.expireddate,
               3,
               'discount-change.jpg'
        FROM public.discount d
        WHERE d.activedate <= CURRENT_DATE AND d.expireddate >= CURRENT_DATE;
    END IF;
    RETURN NULL;
END;
$$;
 -   DROP FUNCTION public.check_daily_discount();
       public          postgres    false            �            1255    33503    check_voucher_expiry()    FUNCTION     R  CREATE FUNCTION public.check_voucher_expiry() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.expireddate - CURRENT_DATE = 1 THEN
        INSERT INTO public.notification (idaccount, title, notification_text, idrole, image)
        SELECT v.idaccount, 
               'Phiếu giảm giá sắp hết hạn',
               'Voucher ' || v.name || ' của bạn còn 1 ngày để sử dụng, hãy mau sử dụng đi nào !!',
               3,
               'voucher-change.jpg'
        FROM public.voucher v
        WHERE v.id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$;
 -   DROP FUNCTION public.check_voucher_expiry();
       public          postgres    false                       1255    33504    handle_order_status_change()    FUNCTION       CREATE FUNCTION public.handle_order_status_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.idstatus <> OLD.idstatus THEN
        IF NEW.idstatus = 2 AND OLD.idstatus = 1 THEN
            INSERT INTO public.notification (idaccount, title, notification_text, idrole, image)
            VALUES (
                NEW.idaccount,
                'Đơn hàng có sự thay đổi',
                'Đơn hàng của bạn đã được nhân viên xác nhận!',
                3,
                'orders-change.jpg'
            );

        ELSIF NEW.idstatus = 5 AND OLD.idstatus = 1 THEN
            INSERT INTO public.notification (idaccount, title, notification_text, idrole, image)
            VALUES (
                NEW.idaccount,
                'Đơn hàng có sự thay đổi',
                'Đơn hàng của bạn đã bị hủy!',
                3,
                'orders-change.jpg'
            );

        ELSIF NEW.idstatus = 3 AND OLD.idstatus = 2 THEN
            INSERT INTO public.notification (idaccount, title, notification_text, idrole, image)
            VALUES (
                NEW.idaccount,
                'Đơn hàng có sự thay đổi',
                'Đơn hàng của bạn đang trên đường đến chỗ bạn!',
                3,
                'orders-change.jpg'
            );

        ELSIF NEW.idstatus = 4 AND OLD.idstatus = 3 THEN
            INSERT INTO public.notification (idaccount, title, notification_text, idrole, image)
            VALUES (
                NEW.idaccount,
                'Đơn hàng có sự thay đổi',
                'Đơn hàng của bạn đã được giao thành công!',
                3,
                'orders-change.jpg'
            );
        END IF;
    END IF;

    RETURN NEW;
END;
$$;
 3   DROP FUNCTION public.handle_order_status_change();
       public          postgres    false            
           1255    33505 #   insert_order_success_notification()    FUNCTION     }  CREATE FUNCTION public.insert_order_success_notification() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    notification_text text;
BEGIN
    IF NEW.idstatus = 1 THEN
       
        notification_text := CONCAT(
           'Tổng giá trị đơn hàng: ', to_char(NEW.totalamount, 'FM999,999,999,999'), 'đ'
        );

        INSERT INTO public.notification (idaccount, title, notification_text, idrole, image)
        VALUES (
            NEW.idaccount, 
            'Đặt hàng thành công',
            notification_text,
            3,
            'order-success.jpg'
        );
    END IF;
    RETURN NEW;
END;
$$;
 :   DROP FUNCTION public.insert_order_success_notification();
       public          postgres    false                       1255    33506    notify_order_status_change()    FUNCTION     O  CREATE FUNCTION public.notify_order_status_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.idstatus <> OLD.idstatus THEN
        IF NEW.idstatus = 2 AND OLD.idstatus = 1 THEN
            INSERT INTO public.notification (idaccount, notification_text)
            SELECT idaccount, 'Đơn hàng của bạn đã được nhân viên xác nhận!'
            FROM public.orders
            WHERE id = NEW.idaccount;

        ELSIF NEW.idstatus = 5 AND OLD.idstatus = 1 THEN
            INSERT INTO public.notification (idaccount, notification_text)
            SELECT idaccount, 'Đơn hàng của bạn đã bị hủy!'
            FROM public.orders
            WHERE id = NEW.idaccount;

        ELSIF NEW.idstatus = 3 AND OLD.idstatus = 2 THEN
            INSERT INTO public.notification (idaccount, notification_text)
            SELECT idaccount, 'Đơn hàng của bạn đang trên đường đến chỗ bạn!'
            FROM public.orders
            WHERE id = NEW.idaccount;

        ELSIF NEW.idstatus = 4 AND OLD.idstatus = 3 THEN
            INSERT INTO public.notification (idaccount, notification_text)
            SELECT idaccount, 'Đơn hàng của bạn đã được giao thành công!'
            FROM public.orders
            WHERE id = NEW.idaccount;
        END IF;
    END IF;

    RETURN NEW;
END;
$$;
 3   DROP FUNCTION public.notify_order_status_change();
       public          postgres    false            	           1255    33810    updatevoucherstatus()    FUNCTION     �   CREATE FUNCTION public.updatevoucherstatus() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE public.voucher
    SET idvoucherstatus = 2
    WHERE id = NEW.idvoucher;

    RETURN NEW;
END;
$$;
 ,   DROP FUNCTION public.updatevoucherstatus();
       public          postgres    false            �            1259    33507    account    TABLE     �  CREATE TABLE public.account (
    id bigint NOT NULL,
    username character varying(100),
    email character varying(200),
    password character varying(200),
    avatar character varying(100),
    firstname character varying(20),
    lastname character varying(20),
    fullname character varying(100),
    phone character varying(15),
    address character varying(500),
    active boolean NOT NULL
);
    DROP TABLE public.account;
       public         heap    postgres    false            �            1259    33512    account_id_seq    SEQUENCE     �   ALTER TABLE public.account ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    214            �            1259    33796    accountslock    TABLE     �   CREATE TABLE public.accountslock (
    id bigint NOT NULL,
    idaccount bigint,
    reason character varying(200),
    bandate timestamp without time zone
);
     DROP TABLE public.accountslock;
       public         heap    postgres    false            �            1259    33795    accountslock_id_seq    SEQUENCE     �   ALTER TABLE public.accountslock ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.accountslock_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    249            �            1259    33513 	   authority    TABLE     c   CREATE TABLE public.authority (
    id bigint NOT NULL,
    idaccount bigint,
    idrole bigint
);
    DROP TABLE public.authority;
       public         heap    postgres    false            �            1259    33516    authority_id_seq    SEQUENCE     �   ALTER TABLE public.authority ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.authority_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    216            �            1259    33517    category    TABLE     p   CREATE TABLE public.category (
    id bigint NOT NULL,
    name character varying(50),
    iddiscount bigint
);
    DROP TABLE public.category;
       public         heap    postgres    false            �            1259    33520    category_id_seq    SEQUENCE     �   ALTER TABLE public.category ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    218            �            1259    33521    discount    TABLE     �   CREATE TABLE public.discount (
    id bigint NOT NULL,
    discountname character varying(100),
    discountnumber character varying(50),
    discountpercent double precision,
    activedate date,
    expireddate date,
    active boolean
);
    DROP TABLE public.discount;
       public         heap    postgres    false            �            1259    33524    discount_id_seq    SEQUENCE     �   ALTER TABLE public.discount ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.discount_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    220            �            1259    33525    feedback    TABLE     �   CREATE TABLE public.feedback (
    id bigint NOT NULL,
    note character varying(200),
    rating integer,
    idaccount bigint,
    idproduct bigint,
    createdate date,
    createtime time without time zone,
    image character varying(100)
);
    DROP TABLE public.feedback;
       public         heap    postgres    false            �            1259    33528    feedback_id_seq    SEQUENCE     �   ALTER TABLE public.feedback ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.feedback_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    222            �            1259    33529    notification    TABLE       CREATE TABLE public.notification (
    id bigint NOT NULL,
    idaccount bigint,
    notification_text text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    image character varying(100),
    idrole bigint,
    title character varying(100)
);
     DROP TABLE public.notification;
       public         heap    postgres    false            �            1259    33535    notification_id_seq    SEQUENCE     �   ALTER TABLE public.notification ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    224            �            1259    33536    orderdetails    TABLE     �   CREATE TABLE public.orderdetails (
    id bigint NOT NULL,
    quantity bigint,
    price double precision,
    idorder bigint,
    idproduct bigint,
    idsize bigint
);
     DROP TABLE public.orderdetails;
       public         heap    postgres    false            �            1259    33539    orderdetails_id_seq    SEQUENCE     �   ALTER TABLE public.orderdetails ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orderdetails_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    226            �            1259    33540    orders    TABLE     Y  CREATE TABLE public.orders (
    id bigint NOT NULL,
    address character varying(200),
    createdate date,
    note character varying(200),
    idaccount bigint,
    idstatus bigint,
    createtime time without time zone,
    totalamount double precision,
    idvoucher bigint,
    distance double precision,
    qr character varying(255)
);
    DROP TABLE public.orders;
       public         heap    postgres    false            �            1259    33543    orders_id_seq    SEQUENCE     �   ALTER TABLE public.orders ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    228            �            1259    33544    orderstatus    TABLE     ]   CREATE TABLE public.orderstatus (
    id bigint NOT NULL,
    name character varying(100)
);
    DROP TABLE public.orderstatus;
       public         heap    postgres    false            �            1259    33547    orderstatus_id_seq    SEQUENCE     �   ALTER TABLE public.orderstatus ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orderstatus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    230            �            1259    33548    product    TABLE     �   CREATE TABLE public.product (
    id bigint NOT NULL,
    name character varying(200),
    price double precision,
    image character varying(255),
    describe character varying(500),
    createdate date,
    active boolean,
    idcategory bigint
);
    DROP TABLE public.product;
       public         heap    postgres    false            �            1259    33553    product_id_seq    SEQUENCE     �   ALTER TABLE public.product ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    232            �            1259    33554    productsize    TABLE     �   CREATE TABLE public.productsize (
    id bigint NOT NULL,
    idproduct bigint,
    idsize bigint,
    price double precision
);
    DROP TABLE public.productsize;
       public         heap    postgres    false            �            1259    33557    productsize_id_seq    SEQUENCE     �   ALTER TABLE public.productsize ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.productsize_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    234            �            1259    33558    role    TABLE     V   CREATE TABLE public.role (
    id bigint NOT NULL,
    name character varying(100)
);
    DROP TABLE public.role;
       public         heap    postgres    false            �            1259    33561    role_id_seq    SEQUENCE     �   ALTER TABLE public.role ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    236            �            1259    33562    size    TABLE     T   CREATE TABLE public.size (
    id bigint NOT NULL,
    name character varying(5)
);
    DROP TABLE public.size;
       public         heap    postgres    false            �            1259    33565    size_id_seq    SEQUENCE     �   ALTER TABLE public.size ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.size_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    238            �            1259    33566    token    TABLE     �   CREATE TABLE public.token (
    id bigint NOT NULL,
    token character varying(50),
    tokentype character varying(50),
    active boolean,
    expireddate timestamp without time zone,
    idaccount bigint
);
    DROP TABLE public.token;
       public         heap    postgres    false            �            1259    33569    token_id_seq    SEQUENCE     �   ALTER TABLE public.token ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    240            �            1259    33570    voucher    TABLE     O  CREATE TABLE public.voucher (
    id bigint NOT NULL,
    name character varying(100),
    number character varying(50),
    createdate date,
    expireddate date,
    price double precision,
    condition double precision,
    idvoucherstatus bigint,
    idaccount bigint,
    icon character varying(100),
    idvouchertype bigint
);
    DROP TABLE public.voucher;
       public         heap    postgres    false            �            1259    33573    voucher_id_seq    SEQUENCE     �   ALTER TABLE public.voucher ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.voucher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    242            �            1259    33574    voucherstatus    TABLE     _   CREATE TABLE public.voucherstatus (
    id bigint NOT NULL,
    name character varying(100)
);
 !   DROP TABLE public.voucherstatus;
       public         heap    postgres    false            �            1259    33577    voucherstatus_id_seq    SEQUENCE     �   ALTER TABLE public.voucherstatus ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.voucherstatus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    244            �            1259    33578    vouchertype    TABLE     ]   CREATE TABLE public.vouchertype (
    id bigint NOT NULL,
    name character varying(255)
);
    DROP TABLE public.vouchertype;
       public         heap    postgres    false            �            1259    33581    vouchertype_id_seq    SEQUENCE     �   ALTER TABLE public.vouchertype ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.vouchertype_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    246            �          0    33507    account 
   TABLE DATA              COPY public.account (id, username, email, password, avatar, firstname, lastname, fullname, phone, address, active) FROM stdin;
    public          postgres    false    214   ��       �          0    33796    accountslock 
   TABLE DATA           F   COPY public.accountslock (id, idaccount, reason, bandate) FROM stdin;
    public          postgres    false    249   �       �          0    33513 	   authority 
   TABLE DATA           :   COPY public.authority (id, idaccount, idrole) FROM stdin;
    public          postgres    false    216   x�       �          0    33517    category 
   TABLE DATA           8   COPY public.category (id, name, iddiscount) FROM stdin;
    public          postgres    false    218   ŷ       �          0    33521    discount 
   TABLE DATA           v   COPY public.discount (id, discountname, discountnumber, discountpercent, activedate, expireddate, active) FROM stdin;
    public          postgres    false    220   �       �          0    33525    feedback 
   TABLE DATA           i   COPY public.feedback (id, note, rating, idaccount, idproduct, createdate, createtime, image) FROM stdin;
    public          postgres    false    222   ��       �          0    33529    notification 
   TABLE DATA           j   COPY public.notification (id, idaccount, notification_text, created_at, image, idrole, title) FROM stdin;
    public          postgres    false    224   ,�       �          0    33536    orderdetails 
   TABLE DATA           W   COPY public.orderdetails (id, quantity, price, idorder, idproduct, idsize) FROM stdin;
    public          postgres    false    226   ��       �          0    33540    orders 
   TABLE DATA           �   COPY public.orders (id, address, createdate, note, idaccount, idstatus, createtime, totalamount, idvoucher, distance, qr) FROM stdin;
    public          postgres    false    228   ��       �          0    33544    orderstatus 
   TABLE DATA           /   COPY public.orderstatus (id, name) FROM stdin;
    public          postgres    false    230   ��       �          0    33548    product 
   TABLE DATA           c   COPY public.product (id, name, price, image, describe, createdate, active, idcategory) FROM stdin;
    public          postgres    false    232   �       �          0    33554    productsize 
   TABLE DATA           C   COPY public.productsize (id, idproduct, idsize, price) FROM stdin;
    public          postgres    false    234   s�       �          0    33558    role 
   TABLE DATA           (   COPY public.role (id, name) FROM stdin;
    public          postgres    false    236   $�       �          0    33562    size 
   TABLE DATA           (   COPY public.size (id, name) FROM stdin;
    public          postgres    false    238   \�       �          0    33566    token 
   TABLE DATA           U   COPY public.token (id, token, tokentype, active, expireddate, idaccount) FROM stdin;
    public          postgres    false    240   ��       �          0    33570    voucher 
   TABLE DATA           �   COPY public.voucher (id, name, number, createdate, expireddate, price, condition, idvoucherstatus, idaccount, icon, idvouchertype) FROM stdin;
    public          postgres    false    242   ��       �          0    33574    voucherstatus 
   TABLE DATA           1   COPY public.voucherstatus (id, name) FROM stdin;
    public          postgres    false    244   O�       �          0    33578    vouchertype 
   TABLE DATA           /   COPY public.vouchertype (id, name) FROM stdin;
    public          postgres    false    246   ��       �           0    0    account_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.account_id_seq', 16, true);
          public          postgres    false    215            �           0    0    accountslock_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.accountslock_id_seq', 5, true);
          public          postgres    false    248            �           0    0    authority_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.authority_id_seq', 16, true);
          public          postgres    false    217            �           0    0    category_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.category_id_seq', 3, true);
          public          postgres    false    219            �           0    0    discount_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.discount_id_seq', 24, true);
          public          postgres    false    221            �           0    0    feedback_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.feedback_id_seq', 42, true);
          public          postgres    false    223            �           0    0    notification_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.notification_id_seq', 182, true);
          public          postgres    false    225            �           0    0    orderdetails_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.orderdetails_id_seq', 101, true);
          public          postgres    false    227            �           0    0    orders_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.orders_id_seq', 70, true);
          public          postgres    false    229            �           0    0    orderstatus_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.orderstatus_id_seq', 5, true);
          public          postgres    false    231            �           0    0    product_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.product_id_seq', 112, true);
          public          postgres    false    233            �           0    0    productsize_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.productsize_id_seq', 47, true);
          public          postgres    false    235            �           0    0    role_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.role_id_seq', 3, true);
          public          postgres    false    237            �           0    0    size_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.size_id_seq', 3, true);
          public          postgres    false    239            �           0    0    token_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.token_id_seq', 15, true);
          public          postgres    false    241            �           0    0    voucher_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.voucher_id_seq', 75, true);
          public          postgres    false    243            �           0    0    voucherstatus_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.voucherstatus_id_seq', 3, true);
          public          postgres    false    245            �           0    0    vouchertype_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.vouchertype_id_seq', 3, true);
          public          postgres    false    247            �           2606    33583    account account_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.account DROP CONSTRAINT account_pkey;
       public            postgres    false    214            �           2606    33800    accountslock accountslock_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.accountslock
    ADD CONSTRAINT accountslock_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.accountslock DROP CONSTRAINT accountslock_pkey;
       public            postgres    false    249            �           2606    33585    authority authority_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.authority
    ADD CONSTRAINT authority_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.authority DROP CONSTRAINT authority_pkey;
       public            postgres    false    216            �           2606    33587    category category_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.category DROP CONSTRAINT category_pkey;
       public            postgres    false    218            �           2606    33589    discount discount_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.discount DROP CONSTRAINT discount_pkey;
       public            postgres    false    220            �           2606    33591    feedback feedback_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.feedback DROP CONSTRAINT feedback_pkey;
       public            postgres    false    222            �           2606    33593    notification notification_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.notification DROP CONSTRAINT notification_pkey;
       public            postgres    false    224            �           2606    33595    orderdetails orderdetails_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.orderdetails
    ADD CONSTRAINT orderdetails_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.orderdetails DROP CONSTRAINT orderdetails_pkey;
       public            postgres    false    226            �           2606    33597    orders orders_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_pkey;
       public            postgres    false    228            �           2606    33599    orderstatus orderstatus_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.orderstatus
    ADD CONSTRAINT orderstatus_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.orderstatus DROP CONSTRAINT orderstatus_pkey;
       public            postgres    false    230            �           2606    33601    product product_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.product DROP CONSTRAINT product_pkey;
       public            postgres    false    232            �           2606    33603    productsize productsize_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.productsize
    ADD CONSTRAINT productsize_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.productsize DROP CONSTRAINT productsize_pkey;
       public            postgres    false    234            �           2606    33605    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public            postgres    false    236            �           2606    33607    size size_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.size
    ADD CONSTRAINT size_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.size DROP CONSTRAINT size_pkey;
       public            postgres    false    238            �           2606    33609    token token_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.token DROP CONSTRAINT token_pkey;
       public            postgres    false    240            �           2606    33611    voucher voucher_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.voucher
    ADD CONSTRAINT voucher_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.voucher DROP CONSTRAINT voucher_pkey;
       public            postgres    false    242            �           2606    33613     voucherstatus voucherstatus_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.voucherstatus
    ADD CONSTRAINT voucherstatus_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.voucherstatus DROP CONSTRAINT voucherstatus_pkey;
       public            postgres    false    244            �           2606    33615    vouchertype vouchertype_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.vouchertype
    ADD CONSTRAINT vouchertype_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.vouchertype DROP CONSTRAINT vouchertype_pkey;
       public            postgres    false    246            �           1259    33794    fki_category_iddiscount_fkey    INDEX     W   CREATE INDEX fki_category_iddiscount_fkey ON public.category USING btree (iddiscount);
 0   DROP INDEX public.fki_category_iddiscount_fkey;
       public            postgres    false    218            �           1259    33725    fki_i    INDEX     =   CREATE INDEX fki_i ON public.orders USING btree (idvoucher);
    DROP INDEX public.fki_i;
       public            postgres    false    228            �           1259    33731    fki_notification_idrole_fkey    INDEX     W   CREATE INDEX fki_notification_idrole_fkey ON public.notification USING btree (idrole);
 0   DROP INDEX public.fki_notification_idrole_fkey;
       public            postgres    false    224            �           1259    33617    voucher_idvouchertype_fkey    INDEX     W   CREATE INDEX voucher_idvouchertype_fkey ON public.voucher USING btree (idvouchertype);
 .   DROP INDEX public.voucher_idvouchertype_fkey;
       public            postgres    false    242                        2620    33811    orders applyvoucher    TRIGGER     �   CREATE TRIGGER applyvoucher AFTER INSERT ON public.orders FOR EACH ROW WHEN ((new.idvoucher IS NOT NULL)) EXECUTE FUNCTION public.updatevoucherstatus();
 ,   DROP TRIGGER applyvoucher ON public.orders;
       public          postgres    false    228    228    265            �           2620    33786 0   discount check_daily_discount_activedate_trigger    TRIGGER     �   CREATE TRIGGER check_daily_discount_activedate_trigger AFTER INSERT OR UPDATE OF activedate ON public.discount FOR EACH STATEMENT EXECUTE FUNCTION public.check_daily_discount();
 I   DROP TRIGGER check_daily_discount_activedate_trigger ON public.discount;
       public          postgres    false    264    220    220            �           2620    33787 1   discount check_daily_discount_expireddate_trigger    TRIGGER     �   CREATE TRIGGER check_daily_discount_expireddate_trigger AFTER INSERT OR UPDATE OF expireddate ON public.discount FOR EACH STATEMENT EXECUTE FUNCTION public.check_daily_discount();
 J   DROP TRIGGER check_daily_discount_expireddate_trigger ON public.discount;
       public          postgres    false    220    220    264                       2620    33784 $   voucher check_voucher_expiry_trigger    TRIGGER     �   CREATE TRIGGER check_voucher_expiry_trigger AFTER INSERT OR UPDATE OF expireddate ON public.voucher FOR EACH ROW EXECUTE FUNCTION public.check_voucher_expiry();
 =   DROP TRIGGER check_voucher_expiry_trigger ON public.voucher;
       public          postgres    false    250    242    242                       2620    33735 "   orders order_status_change_trigger    TRIGGER     �   CREATE TRIGGER order_status_change_trigger AFTER UPDATE OF idstatus ON public.orders FOR EACH ROW EXECUTE FUNCTION public.handle_order_status_change();
 ;   DROP TRIGGER order_status_change_trigger ON public.orders;
       public          postgres    false    263    228    228                       2620    33809    orders order_success_trigger    TRIGGER     �   CREATE TRIGGER order_success_trigger AFTER INSERT ON public.orders FOR EACH ROW EXECUTE FUNCTION public.insert_order_success_notification();
 5   DROP TRIGGER order_success_trigger ON public.orders;
       public          postgres    false    228    266            �           2606    33801 (   accountslock accountslock_idaccount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.accountslock
    ADD CONSTRAINT accountslock_idaccount_fkey FOREIGN KEY (idaccount) REFERENCES public.account(id) ON DELETE CASCADE;
 R   ALTER TABLE ONLY public.accountslock DROP CONSTRAINT accountslock_idaccount_fkey;
       public          postgres    false    3266    249    214            �           2606    33624 "   authority authority_idaccount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.authority
    ADD CONSTRAINT authority_idaccount_fkey FOREIGN KEY (idaccount) REFERENCES public.account(id) ON DELETE CASCADE;
 L   ALTER TABLE ONLY public.authority DROP CONSTRAINT authority_idaccount_fkey;
       public          postgres    false    3266    216    214            �           2606    33629    authority authority_idrole_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.authority
    ADD CONSTRAINT authority_idrole_fkey FOREIGN KEY (idrole) REFERENCES public.role(id) ON DELETE CASCADE;
 I   ALTER TABLE ONLY public.authority DROP CONSTRAINT authority_idrole_fkey;
       public          postgres    false    3291    216    236            �           2606    33789 !   category category_iddiscount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_iddiscount_fkey FOREIGN KEY (iddiscount) REFERENCES public.discount(id) ON UPDATE CASCADE ON DELETE CASCADE;
 K   ALTER TABLE ONLY public.category DROP CONSTRAINT category_iddiscount_fkey;
       public          postgres    false    220    218    3273            �           2606    33639     feedback feedback_idaccount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_idaccount_fkey FOREIGN KEY (idaccount) REFERENCES public.account(id) ON DELETE CASCADE;
 J   ALTER TABLE ONLY public.feedback DROP CONSTRAINT feedback_idaccount_fkey;
       public          postgres    false    222    214    3266            �           2606    33644     feedback feedback_idproduct_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_idproduct_fkey FOREIGN KEY (idproduct) REFERENCES public.product(id) ON DELETE CASCADE;
 J   ALTER TABLE ONLY public.feedback DROP CONSTRAINT feedback_idproduct_fkey;
       public          postgres    false    3287    232    222            �           2606    33649 (   notification notification_idaccount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_idaccount_fkey FOREIGN KEY (idaccount) REFERENCES public.account(id) ON DELETE CASCADE;
 R   ALTER TABLE ONLY public.notification DROP CONSTRAINT notification_idaccount_fkey;
       public          postgres    false    224    3266    214            �           2606    33726 %   notification notification_idrole_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_idrole_fkey FOREIGN KEY (idrole) REFERENCES public.role(id) ON UPDATE CASCADE ON DELETE CASCADE;
 O   ALTER TABLE ONLY public.notification DROP CONSTRAINT notification_idrole_fkey;
       public          postgres    false    3291    224    236            �           2606    33654 &   orderdetails orderdetails_idorder_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orderdetails
    ADD CONSTRAINT orderdetails_idorder_fkey FOREIGN KEY (idorder) REFERENCES public.orders(id) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.orderdetails DROP CONSTRAINT orderdetails_idorder_fkey;
       public          postgres    false    226    228    3283            �           2606    33659 (   orderdetails orderdetails_idproduct_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orderdetails
    ADD CONSTRAINT orderdetails_idproduct_fkey FOREIGN KEY (idproduct) REFERENCES public.product(id) ON DELETE CASCADE;
 R   ALTER TABLE ONLY public.orderdetails DROP CONSTRAINT orderdetails_idproduct_fkey;
       public          postgres    false    232    3287    226            �           2606    33664 %   orderdetails orderdetails_idsize_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orderdetails
    ADD CONSTRAINT orderdetails_idsize_fkey FOREIGN KEY (idsize) REFERENCES public.size(id);
 O   ALTER TABLE ONLY public.orderdetails DROP CONSTRAINT orderdetails_idsize_fkey;
       public          postgres    false    3293    238    226            �           2606    33669    orders orders_idaccount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_idaccount_fkey FOREIGN KEY (idaccount) REFERENCES public.account(id) ON DELETE CASCADE;
 F   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_idaccount_fkey;
       public          postgres    false    228    214    3266            �           2606    33674     orders orders_idorderstatus_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_idorderstatus_fkey FOREIGN KEY (idstatus) REFERENCES public.orderstatus(id) ON DELETE CASCADE;
 J   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_idorderstatus_fkey;
       public          postgres    false    228    3285    230            �           2606    33720    orders orders_idvoucher_fkey    FK CONSTRAINT        ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_idvoucher_fkey FOREIGN KEY (idvoucher) REFERENCES public.voucher(id);
 F   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_idvoucher_fkey;
       public          postgres    false    242    228    3298            �           2606    33679    product product_idcategory_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_idcategory_fkey FOREIGN KEY (idcategory) REFERENCES public.category(id);
 I   ALTER TABLE ONLY public.product DROP CONSTRAINT product_idcategory_fkey;
       public          postgres    false    232    3270    218            �           2606    33689 &   productsize productsize_idproduct_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.productsize
    ADD CONSTRAINT productsize_idproduct_fkey FOREIGN KEY (idproduct) REFERENCES public.product(id) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.productsize DROP CONSTRAINT productsize_idproduct_fkey;
       public          postgres    false    232    3287    234            �           2606    33694 #   productsize productsize_idsize_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.productsize
    ADD CONSTRAINT productsize_idsize_fkey FOREIGN KEY (idsize) REFERENCES public.size(id) ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.productsize DROP CONSTRAINT productsize_idsize_fkey;
       public          postgres    false    234    3293    238            �           2606    33699    token token_idaccount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_idaccount_fkey FOREIGN KEY (idaccount) REFERENCES public.account(id) ON DELETE CASCADE;
 D   ALTER TABLE ONLY public.token DROP CONSTRAINT token_idaccount_fkey;
       public          postgres    false    3266    214    240            �           2606    33704    voucher voucher_idaccount_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.voucher
    ADD CONSTRAINT voucher_idaccount_fkey FOREIGN KEY (idaccount) REFERENCES public.account(id) ON DELETE CASCADE;
 H   ALTER TABLE ONLY public.voucher DROP CONSTRAINT voucher_idaccount_fkey;
       public          postgres    false    3266    242    214            �           2606    33709 $   voucher voucher_idvoucherstatus_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.voucher
    ADD CONSTRAINT voucher_idvoucherstatus_fkey FOREIGN KEY (idvoucherstatus) REFERENCES public.voucherstatus(id) ON DELETE CASCADE;
 N   ALTER TABLE ONLY public.voucher DROP CONSTRAINT voucher_idvoucherstatus_fkey;
       public          postgres    false    242    3300    244            �           2606    33714 "   voucher voucher_idvouchertype_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.voucher
    ADD CONSTRAINT voucher_idvouchertype_fkey FOREIGN KEY (idvouchertype) REFERENCES public.vouchertype(id) ON UPDATE CASCADE ON DELETE CASCADE;
 L   ALTER TABLE ONLY public.voucher DROP CONSTRAINT voucher_idvouchertype_fkey;
       public          postgres    false    246    242    3302            �   �  x��Uˮ�V�~�+eH�}����\�Ƙ�21�/�상�:K�aU�IUU�)�z�4�������ܔ&U�^���^�}8́��=�bπ��"�Y���}-�d�W
����Z�=���CkF6S��jG��9hmS3{������k�L<�%)����G��Pm����5��cJ-�:�iO"6lk8���Uo'b����+!�M�_�	X����jM帛��&�0\�뗝`�ӰU_N�!EQtgm7�6p܁�mO�!�M ��W��O(��=��\"��4`��(� "b��?u[�j����ݱf���n�eZ_�S�a*�4lV��>�;¤>əl߿=����k�?>Dx�4Ë8�`BP���'<�C׷)b٘������c@҂�
4/2�� ��ڜ7�Z�h-��7�F\<Szl��������]��� ��ʔ�g�2��[�����P�Ϸ�Q`��;:��ӫ�)�j��e�Ǭ�.�e�>����#��;
Fe[$�ۣ㘍X�I�͊SN��Z���*� iG�S0�e$�Hʟ I�݋����K�.�����:�U�|�������ۨ4��7N��Ex�do����q�9��;.*�T2�*��b9|-V��Ex�:��Q������ک.\(��x�xc�(nB��X*�Ciu����Ǒ/'�Ŗ���D-�:��߶'�o�&�����vZ�_�ml�qa�iP��wa�+vF =Ͼ.1�ġJ�R����@����{^����F )~b�m"J��RK$^�U��z�t����V9�s���I�<�x���:�j�jE�_�-h��xA�h
��x���ӫ�����NyX,�_�kT� �6Z��W��:���zB��j�!��V8]L�Ȫ��X��6� ��@��&@�$X�|�ʽx<����ۧ�>J���x�����~I��,�~���:�_��z��Y��r7;�]�MX,	� ���k-�ԓX�M�z)5&I�Vګ�ñ��6��20��YW�X�t/�����E�Ta�_��З���ί�l��'���8
|�%f��~���5�ӭ��t1��۠�xI�P��,`2�?��o��Z��ͅ��Qр�5��6Ǆ�5kz-���qXH�^:�|�7�}� 4am����� x�
�&���v8��������]e���6e��!�ѣŁYOkQ���+,�u�/���,��eΪf�R��[�����y}��\ǣ�����K���������      �   ~   x�]ͻ�0�Z���)RΒư �Jcd ���X�Tq�������
9��3���	Y$�*�ޏk��}\7TM�;l� 9����XL+� ���/��q����(��c�n���AJ&s��@ xB\)�      �   =   x����0��0U����t�9��$t�XE���/^�l�`�
���#y��q��&-}���
�      �   3   x�3�t>�@! ��*�?.#ΐ��@,cka^��o����y� �=... ��r      �   t  x�m�=N�@F��)|�D;��u�J��(�#�}��5���$�	��%��G�4����l?%�����2�=�A@Zi�A�A9��t�����9�����sj�䴡����X�6�-h�[sw�i����m��^��9����k<�x +��Үk<�x+���nj<�x'���nE�)pB�+��Y(R�@���hE�J�_�Yd�4�@���E�K#�P�_d�4�@�LЍad�4�@�LЍcd�4�@H��XF�L#	�T�gd�4P��
�Q�Y5Tkm�w���2���a���qwx�?!��y�^��WDϥ�s��r��x����O?#������`��4�U��������I��8׵���V�?x�v]�����      �   �  x�uTKn�@\���`Z��ݽCAb�"�g��=������ ,"�1h�Y�K���7�2�$H^���UU��Dχ�m���p������L�2�J�E� F�S:'Q_K�	A�p�Z_�+�D��#F5#����ó}<������
�J:8Ge:t��^�]~)�w��ν(;{�~�ǧC�����&R\S��¡8)���ޟ��{Wf�$�9�'#��g�l�or������kn�U��SW��iqDB��uKdNhu�s� 3h��l��o+hRӛ��k��P��Zc_�T*��/E�(!�JI1���#h��(��c���L�ʤ�L�������}�� ��D�D+�(�>uP��%o�kP��Ё6�e��c������ʘIQ�VEd�y\E�$8$�`(�}�B%��RqJc�"5f�$��FG��"!~���u:�>jl�U]��.�o�k����>�m�T"�;E�B�
�T��%H���1�+'�Yo�jU�:Y/Wv��*^�9c=�m5��(�Y��l�
��Ȟ�X��/�=+���(G�$O���*��1��¶#����x�L)��ʤ	�t|�,Lmb��M�?�0��w~w�W!��6M����l�\�X}      �   �  x�՝�n���˧��t�]�]�3�8@2�G�z�p7���@�X��#bt0��H�=䰂�c�$ճ$Mjfw��fH0${E�������.�����y1]������q����>y�rL�,�V?���_��w��o_�/�7/&���/��H+m
0�R@�Z�G'gO���O����?���]��7��z��b1����ف�I�z}��zŖ��ϋ���bu�>X_��@]�P:�C������+Q��a�U,��{Ry�z��|=/N�8.�=9�{GG/Vo��o�����Ž_0L����B,T�0:=;y�l��͡G�Ȧ�=!��[���v����������O�E[LW?oa��ʆJ��;�����t�v��pņ��L�������o�����?pw��][cr�Z�j��>=[�*~G�(�}}��x�sB&����.^T�֥Co�+`ڄJ�J���H��;�I:|uR|<>.�c�[��a�5���7�F�6MC�5����rR�z��˼�t���x0�?���\���AU�h �ߠ6�&��`��v�|���}>O�Ƌ6'j��q�]�6#�\�Wڗ�j0"��3Ng+�1��������i*K|�U�ތ��<�LϲO��> �!@�2~ �>H�_HP�[1����ɳSKk@[/fM��OnK��P��`i�7
F�����������N��)���g����y���/����W�)����h[G9��b�^�yNc�������xJ(���@���n�KXh+�e@t��<��ON��9I�/$�V�@��e�^��QU|���7�?��8#�����H)���,��1eJ0Ѫ����_��&�ߪ"�);�vѤ_�� W�o�V���)�:�nL�A,;Vږ��u���v�SVme�a
7*[�ir�B�SoB�\�+ܪv�&e���י���+�Jc�*�mr۴?�VI��1x��+Ty*�m���s%��5�d�jzf�+�ŭ�e�R+���|���MMce�g�+;ۯm�4�*��x���Z���_\����ȥ���*�\���m`��������J�aw,W�y��ٸ��İ+'�:'��-U�)�&���ϓ'���/�|�?>�!��x�2�����8p�"��ٶy�jvo #%�����l443��kNJ�b��e<��R,�j���["�U&0J+b�R�.�F`a���?X�C�m0�TIJ��FXlk���b)��� o���  �!81�֠��V�ޣ�`ikF6����H@\���H�j&�K�!�����s������,&��ڻ]=���v�^���G���YLd�����,�8S�[�0y3�=�n��br��仝9�)��3��}�Q����?�F�������g66����J� ��6X��fv������l�a���="),њV͂�<�#�����`3O�Ha�s��Vk�l IYQ��l�<Y#��������G!�u�b�̘%�� (~��{
`�4+�Y�V�V
Km�Qr�����mHMl�ɳ �p��6$�+hrRoC:eڷ'Y�{8Fy�������H�|�F%rg=�@�ǔk��1�\Ƞ� �ͫ���vH޾9�;���#NTN���R���^�J��c[�ehn�$u�}w��["I@c�sˣ��%c��r�#	�WA�A�VFRR�@��e��Ԙ��inM$!��䤹��T��C�r�yRk�1��(G���~�3��Hw37�UH���MPa�� _)�n��L_���t���u}�!��b
��w�`��ߞP65���}��>�b	�i�{��qet
c>��s�Q�;����;��k��%xJ�z�d��������ěR)����b�9��k�C�`E�B_�8�Q�u��-B6�-(�q�-@KGF�
�Dҵp��*��l�-ѽe�A}�xϳ-�y�4!�� v
�.�V��]J�c.m묋-�#d�_-x�NX�W��I�-�`8dvp2��Q���a��;�f��K@V��HnH$zi�:j�*��x�"5���C���8H���E�!��-7��k)��nO8�C�L(����ZRƦ�(Dj:���Y�DX߿VZ[+]�����(I�Z�����/��B\'c�"⺡��K��b0��4Al���}�"%�ݑ�6/���1>�c!�⦤����~��Xa(�����������W�;�6/��b���U���P�ff��ވ�T�뀑}�������%�M����S�>���'W��bm���Gq�ښ��8p�b���G�#{�
�7��k[��6�9�(w<�^Nj#6�6�����R[�ll���J���ᤶH�&u'�&cd�3�T�l��\�L�|�ql��/��m���j�q1�MmQ3�9B�u�X����F,�.Kړ��7���B<�]��|�}�T=�Ҧ�]��P�b��)S
�[�X�f!|~z6������ƈ�3C]�.���$��^�_y�z=����I��t�iJ2Y��Ҵ�����7,�2�e�zN�*u�^�5�,�n�˦�D
�R��k��7\�ͅ�J���LF���p�?�UÙ�r��tCn[�p�R�
}@�w��9��
��{in����\��J�/���!�8u�p�4��Q�f�u���=)����i,�J�e���y������zo,�ݑv�iCT^iS*K��R}�ב�Ř�F�\�2�J**��F�}�w��vɄ�ҏD���Xiw�7]�_m.�@�K�|�X[v�ڰ2��6tu7
�r(^�-�Qmx��I^��1�}�lhl�P
��|E]�V(���x��C����n�vr�����4u��޿��3 >Y����6��yG	�:�gh�
l��'oeޑ�kS��Ó"��R�;OFp[�5�E��Xg��hA��h]�Rz���y����T\�cL�+�o�,�g������eGy      �   �  x�U���D!D�̖� $�l�q,J+3���Z�Fe�a��ҥX���De_h�^��'Z@���B��_����̊�����W�:d��7�G�N�Y8���6�����j�).|{Q�ڑ@f��# � Z�	b�õCN�^��a;"�o$�������L��85_�q0�4AF�=�h�$ڧk�d�Y(��%�dҲt$��[Z����
�hA�ma"��;&"�$D~�!Z���@���Q���g�[=E��ȗ�"\���?#��<�p��7­�6�e�C���7#��ן�N�8ø�D0���uf-X�tϰ���Ks����"�wj��m�C�w�IߏIZܕ�uA@K낀ƩB@�T!����ː���Ħ���'�������x|���wZs��������/      �   
  x���KkG�ϭO�`��s}�^���x16�2Z���h$$��	!��LN!�9� ���h���{�7IU��<W!���gg�WU]����pAg˫w�����/��tB_��Eq���˫�c6�S��{�<>���K���y��!K��$���X�H���㚐�H����h��������_�F����x�e��<*��i�W<��K��9=��G��5��n���O��}XaYH�7t�!_^��ݶ��8�ф;�y��[���)�z�YL�q#J�w��>������!~������4����*����s<���"�:�b�<�Eq�|�ߜ�ly�ѷ�͙3�7���ڇ�4bsm�
�^�TN��`GB��ӭS��W����\o�ߦ�F�$�[p��4�>�Cz���%��,8�<��Hd��Ii�y�ߚBЃ$��Gi���bL4�*PFh�dŶ����x����)�kL�'�:m$��D��7�̛�iwryyNs"�����mă@��{2�t�-� -�/W+C�E��QI9���8S�a� �a���$~��vJH=&�*�#���a�s����Hn@{��x9|�7�Ҁ��N�Fc��	�aUlD"��Uv|��vL����/���X �Kz��u�z�y���W�������U��������h�M���a�����kP|?����}��h���OЀ&�8_�Tlm4#Rtz����J�#̦�/ԑ�C+�j$]�Z�нNj��d��f��V����B�����%���5X��	��p��T�9�=IC O.c�j�X��S�Lb�>�]l���U�OJWG:Nm	�u��HӒ�ǩQ"�� %��2L��{�z��4�K��kK{R��~t���Ꮵ���K�cNV���#rx������yЌaG��v��o!6x�ST�+���65^I�S��+�)�O��嵁��RsȮ�~������辈T�z*r�ዮ�k���~���X���x�7�x�q��ǫ;n8^j�)��/�/=�z;����(�v��;�)v:��S��|i�����u�mc�u���go�e��l����z��l�Vgp�BՏ4��TYf�G�S��gpв����w��ƻ����Mȇ�dϿ��f}�G�wgI��:��u�.li����A�o��<�u%�_+���5���[Y���k�m�f.���[v��bho\���d���v|�wv�@�"�� �o�P ʙd����	q�%�%B�8�H��x�Z3I��
��ſ~�����i�+      �   @   x�3�H�K��K�2�(�ON-.q�9�32
RS�L8]Rs2�R��lSN�ļ�� 3F��� P�Y      �   _  x��X�o��}���l�,�~P�c`2����E."�H�a�E��Z���f�
;1����@O������Q$-�\��X�"[y�{�=��KI��4>�_�Ǿ�1]q'Y�����Hk�����h(����{I��lӳ�7J�'����S\��ا�-;4� ���z/>�F�'<��<�����x�B	8���'���r����E�X_7��v��+G�D�$��ި&��-�m�{9w��D_���Q�������3���#�1�$�CW�b���n
q�D������	�Y��V1��E�DG�P||X����nt���h����0Y �;��εfΥ�\�N�O=���}J���J��G~�D'�U�8�詉~��Q/�@�����A�e��!�:��P��ú�rJ�j2��.�Q.|�W.�Ď߮"�}
���oNJ�n� ��P sJ�4�9Qk�47OJ*������o]�c�JZ�o���d��Hd״���Z��q�f.�f��h���51B��2����A����6�q<gvk��' |p���3�U|`���5�D_Q�'���AxH���N��P�w�5)���������R��ST�-�퇖m����c�����;3+��Ư�e��o�DƽP���XHc����3��@�&�k 8,�,���p�b�+-\o@��-a�&긐��h�������?@)��P��
-���Rl/mT�T���._LҨP�']�|�Q㟯�-R'�,N`��3OУ��Gu���E(�w��l|���0%!}m���#D�z�U�?�����*U4/;~c���G^u�d�5BS�*�]s�c(T��A��m�v�ZL*]�BnK�2Fe�Ky��r�D9ᴼ��CZ �l1��ք˵OMd�ʌ���~������2�)!|��ɾ��㉝���]U���xa����n��Ao`N�h���A�U�X�U�hM���\���!T��r]UҪ���2�4�b)-7h�[l�4p�4����(�˒���(�%>N�ǡ��y�8�]����#:�O��#�]^�j��o��)��SoMO`k��|8~v]����|�׊v�DT�T���k�k��/�dC����{J<�4��0�ќ���tn��yƦeP������ l�P젰�>ze���<���`J���D���r�"s���� ��ϑ2�����EIhL��؜!���#�,�q�,L�*,9�ya���үu�ш���M���Re��髝'���b4�U� Ѯ�æS|?~�RU �z�R�Q�s�j���$e�4���ڄ�YNN,�syK�%�ʄ-�CM��oP�s��i)9}?��%��g׬��;���OkB]�J!z����z�1.bL��Yu����Ii�R��]5���a��ݗi�� `L��[�Y��ە�7#W���|��P:]���6�8�{���0К��Q;7bZ�V�V�L�j]R����	��FN;�a�T����l",U���5�����dqä��tW��hI�n�+�!�j]-�6VK��O�R? J%H2����0GH~4X���SH!�qr�΃�N�U������]5u��`�sO�	z�·r[�9U��-ff�EK۝bʜ�
��S[�H ��Z?3u���|��R�)M���x~�Զ��L�&��^�d�g&v���ܔH\������l����� �!1�^񧨛Ù5u&��=*
��Էe����`t����0z	���4��e0�J�f	��`Z��3�V%L���`�Z'�iW´K0���9L����id0���a�J�#3����a��0�����4��qz�8�N3�_C����F�%����&�b�j.ɹ���W-�-K�����Q�jU˒�;��1ZdAزZٲ$�N.mX�,�[V�[��m五�&ʔ�Ҕ%m�6eV^����,����)It�j�ʒD�\�:DW��^-Q�$Qc3nkl�m�͸���56��fܶ���n�m�r���ܶ�9��nƌ�*�>>����j��:To3��P��5�ڌ�1�f|m���j��ԡ'c��^)����Xc?l���o�<ٚ�Y~s�܂��/{����c�i����������c������o'<t]      �   �   x�E�ɑD1C�r0Sl^~.�#���>��"�z�lD�;�v!�!؄DΆ�Q�ִY���wS�Á��j#���GJsX�ri�)��Ft~nD�e���º�|�׬Bb-��}ea��,����,��Y��87v���??d%7���c�u�4n      �   (   x�3�tL����2�.ILK�2�t.-.��M-����� ���      �      x�3��2���2������� d      �      x������ � �      �   �  x���?N�0��>E/?��3"�!UHY��AU����sp�X8B�r����V�l�ΐDQ�ӓ�/�U)��&��G7x�ue4��R҉�D�í{j��pO���P�'��$iG�J}�n��u�.���]/I�_XM��AҊ�rv�4&l����,\"�G��E$�.cM��l�H�FX�W�R$�"+{9=qyQ���`��9��'I^-�v*��8�fPÔ��5�K+Q�<�4����h�7[�ƅ�i�0"��p�2����+9j^�|>�\�W�yP̄�Q�X�͓�j1z�Y�|ج	�^���"�z��f����U6�:��R���?mr��a�:�zm�7���F��� {��r�y��v�6u]U���oM%�a�ֳw{^��r�m�2�k~v�V��LJ��q!�      �   0   x�3�<2!1/]!����������pqz<ܵ�D!������� b�q      �   =   x�2 ��1	Đối tác
2	Vận chuyển
3	Giảm giá
\.


>{�     