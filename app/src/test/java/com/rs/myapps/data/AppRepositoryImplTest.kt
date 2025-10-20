package com.rs.myapps.data

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.security.MessageDigest

@OptIn(ExperimentalCoroutinesApi::class)
class AppRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockContext = mockk<android.content.Context>()
    private val repository = AppRepositoryImpl(mockContext, testDispatcher)

    private val algorithm = "MD5"
    private val testContent = "test content"
    private val testContentMD5 = "9473fdd0d880a43c21b7778d34872157".chunked(2)
        .map{ it.toInt(16).toByte() }.toByteArray()

    @Test
    fun `calculateCheckSum should return correct checksum for valid file and algorithm`() = runTest(testDispatcher) {
        // Given
        val testFile = File.createTempFile("test", ".apk")
        testFile.writeBytes(testContent.toByteArray())

        // When
        val result = repository.calculateCheckSum(testFile.absolutePath, algorithm)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(testContentMD5.toList(), result.getOrNull()?.toList())

        // Cleanup
        testFile.delete()
    }

    @Test
    fun `calculateCheckSum should handle exact buffer size correctly`() = runTest(testDispatcher) {
        // Given
        val testFile = File.createTempFile("buffer", ".apk")
        val content = "a".repeat(1024) // Exactly 1024 bytes
        testFile.writeBytes(content.toByteArray())

        // When
        val result = repository.calculateCheckSum(testFile.absolutePath, algorithm)

        // Then
        assertTrue(result.isSuccess)
        val expectedDigest = MessageDigest.getInstance(algorithm).digest(content.toByteArray())
        assertEquals(expectedDigest.toList(), result.getOrNull()?.toList())

        // Cleanup
        testFile.delete()
    }

    @Test
    fun `calculateCheckSum should handle large files correctly`() = runTest(testDispatcher) {
        // Given
        val testFile = File.createTempFile("large", ".apk")
        val largeContent = "a".repeat(1024 * 10) // 10KB content
        testFile.writeBytes(largeContent.toByteArray())

        // When
        val result = repository.calculateCheckSum(testFile.absolutePath, algorithm)

        // Then
        assertTrue(result.isSuccess)
        val expectedDigest = MessageDigest.getInstance(algorithm).digest(largeContent.toByteArray())
        assertEquals(expectedDigest.toList(), result.getOrNull()?.toList())

        // Cleanup
        testFile.delete()
    }

    @Test
    fun `calculateCheckSum should return failure for non-existent file`() = runTest(testDispatcher) {
        // Given
        val nonExistentPath = "/non/existent/path.apk"

        // When
        val result = repository.calculateCheckSum(nonExistentPath, algorithm)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `calculateCheckSum should return failure for invalid algorithm`() = runTest(testDispatcher) {
        // Given
        val testFile = File.createTempFile("test", ".apk")
        testFile.writeBytes(testContent.toByteArray())
        val invalidAlgorithm = "INVALID"

        // When
        val result = repository.calculateCheckSum(testFile.absolutePath, invalidAlgorithm)

        // Then
        assertTrue(result.isFailure)

        // Cleanup
        testFile.delete()
    }

    @Test
    fun `calculateCheckSum should handle empty file correctly`() = runTest(testDispatcher) {
        // Given
        val emptyFile = File.createTempFile("empty", ".apk")
        emptyFile.writeBytes(ByteArray(0)) // Ensure it's empty

        // When
        val result = repository.calculateCheckSum(emptyFile.absolutePath, algorithm)

        // Then
        assertTrue(result.isSuccess)
        val expectedDigest = MessageDigest.getInstance(algorithm).digest(ByteArray(0))
        assertEquals(expectedDigest.toList(), result.getOrNull()?.toList())

        // Cleanup
        emptyFile.delete()
    }
}