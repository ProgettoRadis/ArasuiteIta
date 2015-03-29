 ;;
 ; File: TRules.clp
 ; 		This file is part of Tico, an application to create and	perform
 ; 		interactive communication boards to be used by people with
 ; 		severe motor disabilities.
 ; 
 ; Authors: Beatriz Mateo
 ; 
 ; Date: Oct 15, 2007
 ; 
 ; Company: Universidad de Zaragoza, CPS, DIIS
 ;;

(defclass comparationMin tico.rules.TComparationMin)
(defclass comparationMax tico.rules.TComparationMax)
(defclass exist tico.rules.TExist)
(defclass type tico.rules.TType)
(defclass lightness tico.rules.TLightness)


(defrule element-exist
    "Rule to check if element 'attribute' needs to exist or not depends 
    of 'parameter' indication"
	(exist 
        (name ?n)
        (of ?of)
        (parameter ?par&:(eq ?par TRUE))
        (attribute ?atr&:(not (eq ?atr ?par)))
        (message ?mes)
        (OBJECT ?e))
    =>
    (printout out "101-" ?mes "-" ?of "-" ";")
)


(defrule element-type
    "Rule to check if element 'attribute' is the type of 'parameter'"
	(type 
        (name ?n)
        (of ?of)
        ; if parameter is TRUE the requirements have to be the same, 
        ; the rule is launched
        (parameter ?par&:(eq ?par TRUE))
        ; if attribute and parameter are not the same, the requiremets are not to be the same, 
        ; the rule is launched, the advice message is shown
        (attribute ?atr&:(not (eq ?atr ?par)))
        (message ?mes)
        (OBJECT ?t))
    =>
    (printout out "102-" ?mes "-" ?of "-" ?atr "-" ?par "-" ";")
)


(defrule element-comparationMin
    "Rule to check if element 'attribute' is less than 'parameter'"
	(comparationMin 
        (name ?n)
        (of ?of)
        (parameter ?par)
        (attribute ?atr&:(< ?atr ?par))
        (message ?mes)
        (OBJECT ?c))
    =>
    (printout out "103-" ?mes "-" ?of "-" ?atr "-" ?par "-" ";")
)


(defrule element-comparationMax
    "Rule to check if element 'attribute' is greater than 'parameter'"
	(comparationMax
        (name ?n)
        (of ?of)
        (parameter ?par)
        (attribute ?atr&:(> ?atr ?par))
        (message ?mes)
        (OBJECT ?c))
    =>
    (printout out "103-" ?mes "-" ?of "-" ?atr "-" ?par "-" ";")
)


(defrule element-lightness
    "Rule to check if the substract of 'attribute1' and 'attribute2' is less 
    than 'parameter'"
	(lightness
        (name ?n)
        (of ?of)
        (attribute1 ?atr1)
        (attribute2 ?atr2)
        ; if the difference between attributes is less than parameter
        ; then the rule is launched, the advice message is shown
        (parameter ?par&:(> ?par (abs (- ?atr1 ?atr2))))
        (message ?mes)
        (OBJECT ?c))
    =>
    (printout out "104-" ?mes "-" ?of "-" (abs (- ?atr1 ?atr2)) "-" ?par "-" ";")
)
