package com.eloli.inkerbot.api.config;

import java.lang.annotation.*;

@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
@Repeatable (Comments.class)
public @interface Comment {
  String value();
}
