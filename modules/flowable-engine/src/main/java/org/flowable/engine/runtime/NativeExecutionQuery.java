package org.flowable.engine.runtime;

import org.flowable.engine.common.api.query.NativeQuery;

/**
 * Allows querying of {@link Execution}s via native (SQL) queries
 * 
 * @author Bernd Ruecker (camunda)
 */
public interface NativeExecutionQuery extends NativeQuery<NativeExecutionQuery, Execution> {

}
