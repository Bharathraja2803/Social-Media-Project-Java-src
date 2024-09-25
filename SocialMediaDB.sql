--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comment (
    comment_id integer NOT NULL,
    comment_date date NOT NULL,
    comment_time time without time zone NOT NULL,
    comment_user_id integer NOT NULL,
    post_id integer NOT NULL,
    comment_text character varying(1000)
);


ALTER TABLE public.comment OWNER TO postgres;

--
-- Name: comment_id_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comment_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.comment_id_sequence OWNER TO postgres;

--
-- Name: comment_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comment_id_sequence OWNED BY public.comment.comment_id;


--
-- Name: follower; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.follower (
    user_id integer NOT NULL,
    following_user_id integer NOT NULL
);


ALTER TABLE public.follower OWNER TO postgres;

--
-- Name: likes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.likes (
    like_id integer NOT NULL,
    user_id integer NOT NULL,
    post_id integer NOT NULL,
    like_date date NOT NULL,
    like_time time without time zone NOT NULL
);


ALTER TABLE public.likes OWNER TO postgres;

--
-- Name: like_id_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.like_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.like_id_sequence OWNER TO postgres;

--
-- Name: like_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.like_id_sequence OWNED BY public.likes.like_id;


--
-- Name: post; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.post (
    post_id integer NOT NULL,
    user_id integer,
    posted_date date NOT NULL,
    posted_time time without time zone NOT NULL,
    post_content character varying(1000) NOT NULL
);


ALTER TABLE public.post OWNER TO postgres;

--
-- Name: post_id_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.post_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.post_id_sequence OWNER TO postgres;

--
-- Name: post_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.post_id_sequence OWNED BY public.post.post_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    user_name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    birthdate date NOT NULL,
    email_id character varying(255) NOT NULL,
    signup_date date NOT NULL,
    signup_time time without time zone NOT NULL,
    roles character varying(20) NOT NULL,
    is_blocked character(1) DEFAULT 'n'::bpchar NOT NULL,
    CONSTRAINT ck_users_roles CHECK (((roles)::text = ANY ((ARRAY['user'::character varying, 'admin'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: user_id_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_id_sequence OWNER TO postgres;

--
-- Name: user_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_id_sequence OWNED BY public.users.user_id;


--
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comment (comment_id, comment_date, comment_time, comment_user_id, post_id, comment_text) FROM stdin;
1	2024-09-24	23:48:44.760631	1	1	This the comment from admin
\.


--
-- Data for Name: follower; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.follower (user_id, following_user_id) FROM stdin;
3	1
14	1
14	3
14	4
14	5
14	6
14	7
15	1
\.


--
-- Data for Name: likes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.likes (like_id, user_id, post_id, like_date, like_time) FROM stdin;
1	1	1	2024-09-24	23:48:23.530906
4	14	4	2024-09-25	00:53:25.408771
\.


--
-- Data for Name: post; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.post (post_id, user_id, posted_date, posted_time, post_content) FROM stdin;
1	1	2024-09-24	23:48:10.946206	This is the post from admin
3	3	2024-09-25	00:47:28.2424	This is the post after unblocking
4	14	2024-09-25	00:53:20.056172	This is the post on Foods in different stated of India
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, user_name, password, birthdate, email_id, signup_date, signup_time, roles, is_blocked) FROM stdin;
1	Bharathraja R K	123456abcABC!!	2000-03-28	abc@gmail.com	2024-09-24	19:23:14.1447	admin	n
4	Logesh Kumar	#LPY%070u0>3m%5	2003-09-19	logeshkumar262@gmail.com	2024-09-24	19:23:14.1447	user	n
6	Logesh Kumar	V^=/_DP8=A;M-<x	2000-08-17	logeshkumar230@gmail.com	2024-09-24	19:23:14.1447	user	n
7	Nandha Dharan	6Q&8aTLY?wk0p*i	2001-05-11	nandhadharan131@gmail.com	2024-09-24	19:23:14.1447	user	n
8	Ram Ram	Hz@8c*+i1oca8h6	2003-09-19	ramram262@gmail.com	2024-09-24	19:23:14.1447	user	n
9	Nandha Rajan	n@/b(u=4JqDJB>L	2000-03-12	nandharajan72@gmail.com	2024-09-24	19:23:14.1447	user	n
11	Logesh Rajan	Lf+NnyHk>>#$A<7	2002-06-22	logeshrajan173@gmail.com	2024-09-24	19:23:14.1447	user	n
12	Gopi Kumar	gCm8sLL80/M1_?X	2002-06-22	gopikumar173@gmail.com	2024-09-24	19:23:14.1447	user	n
13	Manohar Kumar	O4egd4hrF=7:(v$	2000-11-20	manoharkumar325@gmail.com	2024-09-24	19:23:14.1447	user	n
3	Manohar Dharan	uZeCfd8w08+kXDR	2000-08-17	manohardharan230@gmail.com	2024-09-24	19:23:14.1447	user	n
5	Logesh Kumar	LogeshKumar@113	2001-05-11	logeshkumar131@gmail.com	2024-09-24	19:23:14.1447	user	n
14	nandha murugan	nanDhaMurugan@112	2000-11-11	nandhamurugan@gmail.com	2024-09-25	00:52:20.446126	admin	n
15	TestoneUser	passWord@223	2000-11-21	testoneuser@gmail.com	2024-09-25	01:50:31.226048	user	n
\.


--
-- Name: comment_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comment_id_sequence', 4, true);


--
-- Name: like_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.like_id_sequence', 5, true);


--
-- Name: post_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.post_id_sequence', 5, true);


--
-- Name: user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_sequence', 15, true);


--
-- Name: comment comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (comment_id);


--
-- Name: likes likes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT likes_pkey PRIMARY KEY (like_id);


--
-- Name: post post_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post
    ADD CONSTRAINT post_pkey PRIMARY KEY (post_id);


--
-- Name: users uq_user_email_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uq_user_email_id UNIQUE (email_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: comment fk_comment_post_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT fk_comment_post_id FOREIGN KEY (post_id) REFERENCES public.post(post_id);


--
-- Name: comment fk_comment_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT fk_comment_user_id FOREIGN KEY (comment_user_id) REFERENCES public.users(user_id);


--
-- Name: follower fk_follower_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.follower
    ADD CONSTRAINT fk_follower_user_id FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: follower fk_following_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.follower
    ADD CONSTRAINT fk_following_user_id FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: likes fk_like_post_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT fk_like_post_id FOREIGN KEY (post_id) REFERENCES public.post(post_id);


--
-- Name: likes fk_like_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT fk_like_user_id FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: post fk_post_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post
    ADD CONSTRAINT fk_post_user_id FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- PostgreSQL database dump complete
--

