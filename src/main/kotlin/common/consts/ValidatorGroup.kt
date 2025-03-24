package io.github.llh4github.ksas.common.consts

interface CreateGroup {}
interface UpdateGroup {}
interface DeleteGroup {}
interface QueryGroup {}

interface CreateUpdateGroup : CreateGroup, UpdateGroup {}
