package me.zhengjie.hibernate

import org.hibernate.collection.spi.PersistentCollection
import org.hibernate.collection.spi.PersistentSet
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.metamodel.CollectionClassification
import org.hibernate.persister.collection.CollectionPersister
import org.hibernate.usertype.UserCollectionType

@Suppress("unused")
class KuKuPersistentSet<E>: PersistentSet<E> {

    constructor(): super()

    init {
        if (set == null) set = mutableSetOf()
    }

    constructor(session: SharedSessionContractImplementor) : super(session)

    constructor(session: SharedSessionContractImplementor, set: Set<E>) : super(session, set)

    override fun iterator(): MutableIterator<E> {
        return try {
            super.iterator()
        } catch (e: Exception) {
            set.iterator()
        }
    }

    override fun toArray(): Array<Any?> {
        return try {
            super.toArray()
        } catch (e: Exception) {
            set.toTypedArray()
        }
    }

    override fun <A : Any?> toArray(array: Array<out A>): Array<A> {
        return try {
            super.toArray(array)
        } catch (e: Exception) {
            (set as HashSet).toArray(array)
        }
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return try {
            super.containsAll(elements)
        } catch (e: Exception) {
            set.containsAll(elements)
        }
    }

    override fun toString(): String {
        return try {
            super.toString()
        } catch (e: Exception) {
            set.toString()
        }
    }

    override fun equals(other: Any?): Boolean {
        return try {
            super.equals(other)
        } catch (e: Exception) {
            set.equals(other)
        }
    }

    override fun hashCode(): Int {
        return try {
            super.hashCode()
        } catch (e: Exception) {
            set.hashCode()
        }
    }

    override val size: Int
        get() = try {
            super.size
        } catch (e: Exception) {
            set.size
        }

    override fun add(element: E): Boolean {
        return try {
            super.add(element)
        } catch (e: Exception) {
            set.add(element)
        }
    }

    override fun remove(element: E): Boolean {
        return try {
            super.remove(element)
        } catch (e: Exception) {
            set.remove(element)
        }
    }
}


@Suppress("UNCHECKED_CAST", "REDUNDANT_PROJECTION")
class KuKuSetType: UserCollectionType {

    override fun getClassification(): CollectionClassification {
        return CollectionClassification.SET
    }

    override fun getCollectionClass(): Class<*> {
        return Set::class.java
    }

    override fun instantiate(
        session: SharedSessionContractImplementor,
        persister: CollectionPersister
    ): PersistentCollection<*> {
        return KuKuPersistentSet<Any>(session)
    }

    override fun instantiate(anticipatedSize: Int): Any {
        return mutableSetOf<Any>()
    }

    override fun wrap(session: SharedSessionContractImplementor, collection: Any): PersistentCollection<*> {
        return KuKuPersistentSet<Any>(session, collection as Set<Any>)
    }

    override fun getElementsIterator(collection: Any): MutableIterator<*> {
        val set = collection as MutableSet<*>
        return set.iterator()
    }

    override fun contains(collection: Any, entity: Any): Boolean {
        val set = collection as MutableSet<*>
        return set.contains(entity)
    }

    override fun indexOf(collection: Any?, entity: Any?): Any {
        val set = collection as MutableSet<*>
        return set.indexOf(entity)
    }

    override fun replaceElements(
        original: Any?,
        target: Any?,
        persister: CollectionPersister?,
        owner: Any?,
        copyCache: MutableMap<Any?, Any?>?,
        session: SharedSessionContractImplementor?
    ): Any {
        val set = target as MutableSet<Any>
        set.clear()
        set.addAll(original as Set<out Any>)
        return set
    }
}


