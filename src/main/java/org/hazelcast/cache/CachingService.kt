package org.hazelcast.cache

import com.hazelcast.map.IMap
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.future.await
import org.springframework.data.domain.Sort
import reactor.util.Loggers


class CachingService(private val cache: IMap<Long, Person>, private val repository: PersonRepository) {

    private val logger = Loggers.getLogger(CachingService::class.java)

    suspend fun findById(id: Long) = cache.getAsync(id).await()
        .also {
            if (it == null) logger.info("Person with id $id not found in cache")
            else logger.info("Person with id $id found in cache")
        } ?: repository.findById(id)
            ?.also {
                cache.putAsync(it.id, it)
                logger.info("Person with id $id put in cache")
            }

    suspend fun findAll(sort: Sort) = repository
        .findAll(sort)
        .also { flow ->
            flow.collect {
                cache.putAsync(it.id, it)
            }
        }
}
