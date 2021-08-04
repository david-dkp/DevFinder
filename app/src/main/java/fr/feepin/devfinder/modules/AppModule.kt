package fr.feepin.devfinder.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.feepin.devfinder.data.repos.*

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

    @Binds
    abstract fun bindChatRepository(appChatRepository: AppChatRepository): ChatRepository

    @Binds
    abstract fun bindUserRepository(appUserRepository: AppUserRepository): UserRepository

    @Binds
    abstract fun bindProjectRepository(appProjectRepository: AppProjectRepository): ProjectRepository
}