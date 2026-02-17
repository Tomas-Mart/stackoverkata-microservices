/**
 * Определение id генератора с пулом.
 *
 * Генератор обращается к БД и
 * "бронирует" количество айдишников, равное increment_size. В дальнейшем оптимизатор
 * pooled-lo генерирует промежуточные значения без лишнего обращения к базе.
 *
 * Для корректной работы конфиг должен оставаться в файле package-info.java.
 */
@GenericGenerator(
        name = "ID_GENERATOR_POOLED",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(
                        name = "sequence_name",
                        value = "ID_POOL_SEQUENCE"
                ),
                @Parameter(
                        name = "increment_size",
                        value = "100"
                ),
                @Parameter(
                        name = "optimizer",
                        value = "pooled-lo"
                )
        }
)
package stackover.resource.service.config.hibernate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

