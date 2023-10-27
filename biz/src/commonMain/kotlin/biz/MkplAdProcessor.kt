package ru.otus.otuskotlin.marketplace.biz

import biz.groups.operation
import biz.groups.stubs
import biz.permisions.accessValidation
import biz.permisions.chainPermissions
import biz.permisions.frontPermissions
import biz.permisions.searchTypes
import biz.repo.repoCreate
import biz.repo.repoDelete
import biz.repo.repoPrepareCreate
import biz.repo.repoPrepareDelete
import biz.repo.repoPrepareUpdate
import biz.repo.repoRead
import biz.repo.repoSearch
import biz.repo.repoUpdate
import biz.validation.finishProfileFilterValidation
import biz.validation.finishProfileValidation
import biz.validation.validateDescriptionHasContent
import biz.validation.validateDescriptionNotEmpty
import biz.validation.validateIdNotEmpty
import biz.validation.validateIdProperFormat
import biz.validation.validateLockNotEmpty
import biz.validation.validateLockProperFormat
import biz.validation.validateNameHasContent
import biz.validation.validateTitleNotEmpty
import biz.validation.validation
import biz.workers.initStatus
import biz.workers.stubCreateSuccess
import biz.workers.stubDbError
import biz.workers.stubDeleteSuccess
import biz.workers.stubNoCase
import biz.workers.stubOffersSuccess
import biz.workers.stubReadSuccess
import biz.workers.stubSearchSuccess
import biz.workers.stubUpdateSuccess
import biz.workers.stubValidationBadDescription
import biz.workers.stubValidationBadId
import biz.workers.stubValidationBadTitle
import chain
import rootChain
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.prepareResult
import worker

class MkplProfileProcessor(val settings: MkplCorSettings = MkplCorSettings()) {
    suspend fun exec(ctx: MkplContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<MkplContext> {
            initStatus("Инициализация статуса")
            operation("Создание анкеты", MkplCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { profileValidating = profileRequest.deepCopy() }
                    worker("Очистка id") { profileValidating.id = MkplUserId.NONE }
                    worker("Очистка имени") { profileValidating.name = profileValidating.name.trim() }
                    worker("Очистка описания") { profileValidating.description = profileValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateNameHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")

                    finishProfileValidation("Завершение проверок")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    accessValidation("Вычисление прав доступа")
                    repoCreate("Создание объявления в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Получить анкету", MkplCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { profileValidating = profileRequest.deepCopy() }
                    worker("Очистка id") { profileValidating.id = MkplUserId(profileValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishProfileValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение объявления из БД")
                    accessValidation("Вычисление прав доступа")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == MkplState.RUNNING }
                        handle { profileRepoDone = profileRepoRead }
                    }
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Изменить анкету", MkplCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { profileValidating = profileRequest.deepCopy() }
                    worker("Очистка id") { profileValidating.id = MkplUserId(profileValidating.id.asString().trim()) }
                    worker("Очистка lock") { profileValidating.lock = MkplProfileLock(profileValidating.lock.asString().trim()) }
                    worker("Очистка заголовка") { profileValidating.name = profileValidating.name.trim() }
                    worker("Очистка описания") { profileValidating.description = profileValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateNameHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishProfileValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    accessValidation("Вычисление прав доступа")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление объявления в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Удалить анкету", MkplCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") {
                        profileValidating = profileRequest.deepCopy() }
                    worker("Очистка id") { profileValidating.id = MkplUserId(profileValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    finishProfileValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение объявления из БД")
                    accessValidation("Вычисление прав доступа")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление объявления из БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Поиск анкеты", MkplCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adFilterValidating") { profileFilterValidating = profileFilterRequest.copy() }

                    finishProfileFilterValidation("Успешное завершение процедуры валидации")
                }
                repoSearch("Поиск объявления в БД по фильтру")
                prepareResult("Подготовка ответа")
            }
            operation("Поиск подходящих предложений для анкеты", MkplCommand.OFFERS) {
                stubs("Обработка стабов") {
                    stubOffersSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { profileValidating = profileRequest.deepCopy() }
                    worker("Очистка id") { profileValidating.id = MkplUserId(profileValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishProfileValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                searchTypes("Подготовка поискового запроса")

                repoSearch("Поиск объявления в БД по фильтру")
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
