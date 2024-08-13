--
-- PostgreSQL database dump
--

-- Dumped from database version 14.1 (Debian 14.1-1.pgdg110+1)
-- Dumped by pg_dump version 14.12 (Ubuntu 14.12-0ubuntu0.22.04.1)
--
-- Data for Name: admins; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.admins (id, email, encrypted_password, sign_in_count, current_sign_in_at, last_sign_in_at, current_sign_in_ip, last_sign_in_ip, failed_attempts, unlock_token, locked_at, created_at, updated_at) VALUES (1, 'luke@dreamcode.pl', '{bcrypt}$2a$10$i7G.1YWru9mdurZhXqqytePfIE.Yz7krYQypXFW8cm7mXNDeN6cmu', 1, '2022-11-07 20:36:44.956725', '2022-11-07 20:36:44.956725', '172.21.0.1', '172.21.0.1', 0, NULL, NULL, '2022-11-07 20:29:26.773548', '2022-11-07 20:36:44.957268');


--
-- Data for Name: project_errors; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.project_errors (id, project_name, body, created_at, updated_at) VALUES (2, 'error_notifier', 'Error on page for test', '2024-07-30 08:45:38.281804', '2024-07-30 08:45:38.281804');


--
-- Data for Name: schema_migrations; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.schema_migrations (version) VALUES ('20160622171915');
INSERT INTO public.schema_migrations (version) VALUES ('20160622172459');

--
-- PostgreSQL database dump complete
--

