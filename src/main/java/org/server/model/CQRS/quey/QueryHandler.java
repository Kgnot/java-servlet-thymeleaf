package org.server.model.CQRS.quey;

public interface QueryHandler<TQuery extends Query, TResult> {

    TResult handle(TQuery query);

}

