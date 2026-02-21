package com.asimorphic.core.data.database

import androidx.sqlite.SQLiteException
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result

suspend inline fun <T> executeDatabaseUpdate(
    update: suspend () -> T
): Result<T, DataError.Local> {
    return try {
        Result.Success(data = update())
    } catch (_: SQLiteException) {
        Result.Failure(error = DataError.Local.DISK_FULL)
    }
}