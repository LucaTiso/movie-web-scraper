CREATE TABLE IF NOT EXISTS public.movie_new
(
   
    duration integer,
    metascore integer,
    href character varying(255) COLLATE pg_catalog."default" NOT NULL UNIQUE,
    matascore_num_ratings integer,
    year integer,
    user_rating real,
    user_num_ratings integer,
    id bigint NOT NULL,
    movie_cast character varying(1000) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    plot character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    regia character varying(255) COLLATE pg_catalog."default",
    production character varying(255) COLLATE pg_catalog."default",
    genre character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT movie_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.movie_new
    OWNER to tiso;