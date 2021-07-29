package com.txu.scala.crux

import java.util
import java.util._
import java.util.regex.Pattern

import com.txu.scala.crux.SWildcard.JWildcardToRegex.{JWildcardRule, JWildcardRules}


object SWildcard {


  private var DEFAULT_REGEX_RULES = new JWildcardRules(new HashSet(
    Arrays.asList(new JWildcardRule("?", "."),
      new JWildcardRule("*", ".*"))))


  //Used for one call by one expression
  def matches(wildcard: String, text: String): Boolean = if (text == null || wildcard == null) throw new IllegalArgumentException("Text and Wildcard must not be null")
  else {
    for (wd <- wildcard.split("\\|")) {
      val regex = wildcardToRegex(wd)
      if (Pattern.matches(regex, text)) return true
    }
    false
  }

  //Used for multiple calls by one expression with the func_ splitWildcardToRegexSet
  def matches(regex: scala.collection.immutable.Set[String], text: String): Boolean = if (text == null || regex == null) throw new IllegalArgumentException("Text and Wildcard must not be null")
  else {
    for (re <- regex) {
      if (Pattern.matches(re, text)) return true
    }
    false
  }
  def splitWildcardToRegexSet(wildcard: String) = wildcard.split("\\|").toSet[String].map(wc => wildcardToRegex(wc))
  def wildcardToRegex(wildcard: String): String = wildcardToRegex(wildcard, DEFAULT_REGEX_RULES, true)

  def wildcardToRegex(wildcard: String, rules: JWildcardRules, strict: Boolean): String = JWildcardToRegex.wildcardToRegex(wildcard, rules, strict)


  object JWildcardToRegex {
    def wildcardToRegex(wildcard: String, rules: JWildcardRules, strict: Boolean) = if (wildcard == null) throw new IllegalArgumentException("Wildcard must not be null")
    else if (rules == null) throw new IllegalArgumentException("Rules must not be null")
    else {
      val listOfOccurrences = getContainedWildcardPairsOrdered(wildcard, rules)
      val regex = getRegexString(wildcard, listOfOccurrences)
      if (strict) "^" + regex + "$"
      else regex
    }

    def getRegexString(wildcard: String, listOfOccurrences: util.List[JWildcardRuleWithIndex]) = {
      val regex = new StringBuilder
      var cursor = 0
      var jWildcardRuleWithIndex: JWildcardRuleWithIndex = null
      var index = 0
      val var4 = listOfOccurrences.iterator
      while (var4.hasNext) {
        jWildcardRuleWithIndex = var4.next()
        index = jWildcardRuleWithIndex.index
        if (index != 0) regex.append(Pattern.quote(wildcard.substring(cursor, index)))
        regex.append(jWildcardRuleWithIndex.rule.target)

        cursor = index + jWildcardRuleWithIndex.rule.source.length
      }
      if (cursor <= wildcard.length - 1) regex.append(Pattern.quote(wildcard.substring(cursor, wildcard.length)))
      regex.toString
    }

    def getContainedWildcardPairsOrdered(wildcard: String, rules: JWildcardRules): List[JWildcardRuleWithIndex] = {
      var listOfOccurrences: List[JWildcardRuleWithIndex] = new LinkedList()
      var var3: Iterator[JWildcardRule] = rules.getRules().iterator()

      while (var3.hasNext()) {
        var jWildcardRuleWithIndex: JWildcardRule = var3.next().asInstanceOf[JWildcardRule]
        var index: Int = -1

        do {
          index = wildcard.indexOf(jWildcardRuleWithIndex.source, index + 1);
          if (index > -1) {
            listOfOccurrences.add(new JWildcardRuleWithIndex(jWildcardRuleWithIndex, index));
          }

        } while (index > -1)
      }

      listOfOccurrences.sort(
        new Comparator[JWildcardRuleWithIndex]() {
          override def compare(o1: JWildcardRuleWithIndex, o2: JWildcardRuleWithIndex) =
            if (o1.index == o2.index) 0 else if (o1.index > o2.index) 1 else -1
        })

      listOfOccurrences
    }


    class JWildcardRuleWithIndex(val rule: JWildcardRule, val index: Int) {
    }

    class JWildcardRule(var source: String, var target: String) {
      if (source == null || target == null) throw new IllegalArgumentException("Empty values are not allowed")

      def this(regex: String) {
        this(regex, regex)
      }

      override def hashCode(): Int = super.hashCode()

      override def equals(obj: Any): Boolean = {
        if (obj == this) {
          true
        } else if (obj != null && this.getClass() == obj.getClass()) {
          val that = obj.asInstanceOf[JWildcardRule]
          if (!this.source.equals(that.source)) false else this.target.equals(that.target);
        } else {
          false;
        }
      }
    }

    class JWildcardRules(var rules: Set[JWildcardRule]) {

      if (rules != null) new HashSet(rules) else new HashSet()

      def this() {
        this(new util.HashSet[JWildcardRule]())
      }

      def addRule(rule: JWildcardRule) = {
        if (rule == null) {
          throw new IllegalArgumentException("Rule can't be null")
        } else {
          this.rules.add(rule)
        }
      }

      def addRules(rules: Collection[JWildcardRule]) = {
        if (rules == null) {
          throw new IllegalArgumentException("Rules list can't be null")
        } else {
          this.rules.addAll(rules)
        }
      }

      def removeRule(rule: JWildcardRule) = {
        if (rule == null) {
          throw new IllegalArgumentException("Rule to remove can't be null")
        } else {
          this.rules.remove(rule)
        }
      }

      def getRules(): Set[JWildcardRule] = rules

    }

  }


}