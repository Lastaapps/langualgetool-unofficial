package cz.lastaapps.languagetool.data.logic

import cz.lastaapps.languagetool.domain.logic.textDiff
import cz.lastaapps.languagetool.ui.util.withLength
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MatchTextOperationsKtTest : FunSpec({
    context("textDiff") {

        test("same") {
            textDiff("Hello, world", "Hello, world")
                .shouldBe(0 withLength 0 to "")
        }

        test("t1 longer") {
            textDiff("Hello, world", "Hello")
                .shouldBe(5 withLength 7 to "")
        }
        test("t2 longer") {
            textDiff("Hello", "Hello, world")
                .shouldBe(5 withLength 0 to ", world")
        }

        test("t1 diff start") {
            textDiff("world", "Hello, world")
                .shouldBe(0 withLength 0 to "Hello, ")
        }
        test("t2 diff start") {
            textDiff("Hello, world", "world")
                .shouldBe(0 withLength 7 to "")
        }

        test("insertion") {
            textDiff("Hello world", "Hello, world")
                .shouldBe(5 withLength 0 to ",")
            textDiff("Hell world", "Hello, world")
                .shouldBe(4 withLength 0 to "o,")
        }
        test("removal") {
            textDiff("Hello, world", "Hello world")
                .shouldBe(5 withLength 1 to "")
            textDiff("Hello, world", "Hell world")
                .shouldBe(4 withLength 2 to "")
        }
        test("replace") {
            textDiff("Hello, world", "Hele world")
                .shouldBe(3 withLength 3 to "e")
            textDiff("Hele world", "Hello, world")
                .shouldBe(3 withLength 1 to "lo,")

            textDiff("Hello, world", "Bye, world")
                .shouldBe(0 withLength 5 to "Bye")
            textDiff("Hello, world", "Hello there")
                .shouldBe(5 withLength 7 to " there")
        }
        test("more changes") {
            textDiff("Hello this my great world", "Hello that my bad world")
                .shouldBe(8 withLength 11 to "at my bad")
        }

        test("more tests") {
            textDiff(
                "Thdis sentenci wrong is.",
                "Thddis sentenci wrong is.",
            ).shouldBe(3 withLength 0 to "d")
            textDiff(
                "Thddis sentenci wrong is.",
                "Thdis sentenci wrong is.",
            ).shouldBe(3 withLength 1 to "")
            textDiff(
                "Thdis sentenci wrong is.",
                "Thdddis sentenci wrong is.",
            ).shouldBe(3 withLength 0 to "dd")
            textDiff(
                "Thdddis sentenci wrong is.",
                "Thdis sentenci wrong is.",
            ).shouldBe(3 withLength 2 to "")
        }

        test("in practise") {
            val t1 = "Hello, world"
            val t2 = "Hello darkness my old world"
            val diff = textDiff(t1, t2)
            diff shouldBe (5 withLength 1 to " darkness my old")
            val updated = t1.replaceRange(diff.first, diff.second)
            updated shouldBe t2
        }
    }
})
