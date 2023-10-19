package ru.otus.otuskotlin.marketplace.biz

import biz.groups.operation
import biz.groups.stubs
import biz.validation.finishProfileFilterValidation
import biz.validation.finishProfileValidation
import biz.validation.validateDescriptionHasContent
import biz.validation.validateDescriptionNotEmpty
import biz.validation.validateIdNotEmpty
import biz.validation.validateIdProperFormat
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
import rootChain
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import worker

class MkplProfileProcessor {
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
                    worker("Очистка заголовка") { profileValidating.name = profileValidating.name.trim() }
                    worker("Очистка описания") { profileValidating.description = profileValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateNameHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishProfileValidation("Успешное завершение процедуры валидации")
                }
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
            }
        }.build()
    }
}
