
package com.kelompok13.doodlequest.backend.score;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ScoreRequest(

        @NotNull(message = "Score wajib diisi")
        @Min(value = 0, message = "Score tidak boleh negatif")
        @Max(value = 10000000, message = "Score terlalu besar")
        Integer score

) {
}
