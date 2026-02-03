package ar.com.logiciel.cptmobile.core.di

import ar.com.logiciel.cptmobile.data.repository.*
import ar.com.logiciel.cptmobile.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindVentasRepository(
        ventasRepositoryImpl: VentasRepositoryImpl
    ): VentasRepository

    @Binds
    @Singleton
    abstract fun bindClientesRepository(
        clientesRepositoryImpl: ClientesRepositoryImpl
    ): ClientesRepository

    @Binds
    @Singleton
    abstract fun bindArticulosRepository(
        articulosRepositoryImpl: ArticulosRepositoryImpl
    ): ArticulosRepository

    @Binds
    @Singleton
    abstract fun bindRubrosRepository(
        rubrosRepositoryImpl: RubrosRepositoryImpl
    ): RubrosRepository

    @Binds
    @Singleton
    abstract fun bindVendedoresRepository(
        vendedoresRepositoryImpl: VendedoresRepositoryImpl
    ): VendedoresRepository

    @Binds
    @Singleton
    abstract fun bindProveedoresRepository(
        proveedoresRepositoryImpl: ProveedoresRepositoryImpl
    ): ProveedoresRepository
}
