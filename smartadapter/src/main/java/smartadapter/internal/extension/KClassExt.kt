package smartadapter.internal.extension

import kotlin.reflect.KClass


/*
 * Created by Manne Öhlund on 2020-01-08.
 * Copyright (c) All rights reserved.
 */
 
 internal val <T : Any> KClass<T>.name
    get() = qualifiedName ?: java.name