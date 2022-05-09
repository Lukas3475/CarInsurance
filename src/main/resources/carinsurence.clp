;;******************
;; Global variables*
;;******************

(defglobal ?*percentDiscount* = 0.0)

(defglobal ?*constantDiscount* = 0.0)

(defglobal ?*percentRise* = 1.0)

(defglobal ?*constantRise* = 0.0)

;;***********
;; Templates*
;;***********

(deftemplate percentDiscount
    (slot value (type FLOAT) (default 0.0))
)

(deftemplate constantDiscount
    (slot value (type FLOAT) (default 0.0))
)

(deftemplate percentRise
    (slot value (type FLOAT) (default 0.0))
)

(deftemplate constantRise
    (slot value (type FLOAT) (default 0.0))
)

(deftemplate price-info
    (slot info-type)
    (slot desc (type STRING))
    (slot value)
)

(deftemplate car
    (slot carBrand)
    (slot carModel)
    (slot basePrice)
)

(deftemplate attribute
    (slot variable)
    (slot value)
)

;;*******
;; Facts*
;;*******

(deffacts car-list
    ;;AUDI
    (car (carBrand AUDI) (carModel A-ONE) (basePrice 800))
    (car (carBrand AUDI) (carModel A-THREE) (basePrice 900))
    (car (carBrand AUDI) (carModel A-FOUR) (basePrice 1000))
    ;;BMW
    (car (carBrand BMW) (carModel X-ONE) (basePrice 1000))
    (car (carBrand BMW) (carModel X-THREE) (basePrice 1200))
    (car (carBrand BMW) (carModel X-FOUR) (basePrice 1700))
    ;;OPEL
    (car (carBrand OPEL) (carModel ASTRA) (basePrice 600))
    (car (carBrand OPEL) (carModel CORSA) (basePrice 550))
    (car (carBrand OPEL) (carModel INSIGNIA) (basePrice 700))
)

;;***************************
;; Rules for engine capacity*
;;***************************

(defrule engine-capacity-1-0
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.0)))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.1))
    (assert (price-info (info-type percentRise) (desc "Rise for engine capacity 1.0") (value 0.1)))
)

(defrule engine-capacity-1-2
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.2)))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.15))
    (assert (price-info (info-type percentRise) (desc "Rise for engine capacity 1.2") (value 0.15)))
)

(defrule engine-capacity-1-4
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.4)))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.2))
    (assert (price-info (info-type percentRise) (desc "Rise for engine capacity 1.4") (value 0.2)))
)

(defrule engine-capacity-1-6
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.6)))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.25))
    (assert (price-info (info-type percentRise) (desc "Rise for engine capacity 1.6") (value 0.25)))
)

(defrule engine-capacity-1-8
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.8)))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.3))
    (assert (price-info (info-type percentRise) (desc "Rise for engine capacity 1.8") (value 0.3)))
)

(defrule engine-capacity-2-0
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 2.0)))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.35))
    (assert (price-info (info-type percentRise) (desc "Rise for engine capacity 2.0") (value 0.35)))
)

(defrule engine-capacity-2-2
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 2.2)))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.4))
    (assert (price-info (info-type percentRise) (desc "Rise for engine capacity 2.2") (value 0.4)))
)

;;***************************
;; Rules for production year*
;;***************************

(defrule production-year-new
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable productionYear) (and (> ?value 2019) (< ?value 2023))))
    =>
    (bind ?*percentDiscount* (+ ?*percentDiscount* 0.09))
    (assert (price-info (info-type percentDiscount) (desc "Discount for production year") (value 0.09)))
)

(defrule production-year-old
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable productionYear) (and (> ?value 2000) (< ?value 2019))))
    =>
    (bind ?*percentRise* (+ ?*percentRise* 0.13))
    (assert (price-info (info-type percentRise) (desc "Rise for production year") (value 0.13)))
)

;;************************
;; Rules for licence year*
;;************************

(defrule licence-year
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable licenceYear) (and (> ?value 1) (< ?value 5))))
    =>
    (bind ?*constantRise* (+ ?*constantRise* 300.0))
    (assert (price-info (info-type constantRise) (desc "Rise for production year") (value 300.0)))
)

;;********************************
;; Rules for regular customer age*
;;********************************

(defrule regular-customer-age-6-9
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable regularCustomerAge) (and (> ?value 6) (< ?value 9))))
    =>
    (bind ?*constantDiscount* (+ ?*constantDiscount* 200.0))
    (assert (price-info (info-type constantDiscount) (desc "Discount for regular customer age between 6 9 years") (value 200.0)))
)

(defrule regular-customer-age-more
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable regularCustomerAge) (eq ?value MORE)))
    =>
    (bind ?*constantDiscount* (+ ?*constantDiscount* 250.0))
    (assert (price-info (info-type constantDiscount) (desc "Discount for regular customer age more than 9 years") (value 250.0)))
)

;;************************
;; Rules for had accident*
;;************************

(defrule had-accident-yes
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable hadAccident) (eq ?value YES)))
    =>
    (bind ?*constantRise* (+ ?*constantRise* 650.0))
    (assert (price-info (info-type constantRise) (desc "Rise for having accident") (value 650.0)))
)

(defrule had-accident-no
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable hadAccident) (eq ?value NO)))
    =>
    (bind ?*constantDiscount* (+ ?*constantDiscount* 200.0))
    (assert (price-info (info-type constantDiscount) (desc "Discount for not having accident") (value 200.0)))
)

;;**************************
;; Rules for marital status*
;;**************************

(defrule marital-status-married
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value MARRIED)))
    =>
    (bind ?*constantDiscount* (+ ?*constantDiscount* 100.0))
    (assert (price-info (info-type constantDiscount) (desc "Discount for being married") (value 100.0)))
)

(defrule marital-status-divorced
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value DIVORCED)))
    =>
    (bind ?*constantDiscount* (+ ?*constantDiscount* 45.0))
    (assert (price-info (info-type constantDiscount) (desc "Discount for being divorced") (value 45.0)))
)

(defrule marital-status-widow
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value WIDOW)))
    =>
    (bind ?*constantRise* (+ ?*constantRise* 150.0))
    (assert (price-info (info-type constantRise) (desc "Rise for being widow") (value 150.0)))
)

(defrule marital-status-single
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value SINGLE)))
    =>
    (bind ?*constantRise* (+ ?*constantRise* 300.0))
    (assert (price-info (info-type constantRise) (desc "Rise for being single") (value 300.0)))
)


;;***********
;; Functions*
;;***********

(deffunction getPercentDiscount()
    (assert (percentDiscount (value ?*percentDiscount*)))
)

(deffunction getConstantDiscount()
    (assert (constantDiscount (value ?*constantDiscount*)))
)

(deffunction getPercentRise()
    (assert (percentRise (value ?*percentRise*)))
)

(deffunction getConstantRise()
    (assert (constantRise (value ?*constantRise*)))
)


