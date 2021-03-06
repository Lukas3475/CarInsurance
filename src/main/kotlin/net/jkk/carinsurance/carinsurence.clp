;;***********
;; Templates*
;;***********

(deftemplate price-change
    (slot percentDiscount (type FLOAT))
    (slot constantDiscount (type FLOAT))
    (slot percentRise (type FLOAT))
    (slot constantRise (type FLOAT))
)

(deftemplate price-after
    (slot changed-price (type FLOAT))
)

(deftemplate price-info
    (slot info-type (type STRING))
    (slot desc (type STRING))
    (slot value (type STRING))
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
    ;;PRICE-CHANGE
    (price-change (percentDiscount 0.0) (constantDiscount 0.0) (percentRise 1.0) (constantRise 0.0))
    ;;AUDI
    (car (carBrand AUDI) (carModel A-ONE) (basePrice 800.0))
    (car (carBrand AUDI) (carModel A-THREE) (basePrice 900.0))
    (car (carBrand AUDI) (carModel A-FOUR) (basePrice 1000.0))
    ;;BMW
    (car (carBrand BMW) (carModel X-ONE) (basePrice 1000.0))
    (car (carBrand BMW) (carModel X-THREE) (basePrice 1200.0))
    (car (carBrand BMW) (carModel X-FOUR) (basePrice 1700.0))
    ;;OPEL
    (car (carBrand OPEL) (carModel ASTRA) (basePrice 600.0))
    (car (carBrand OPEL) (carModel CORSA) (basePrice 550.0))
    (car (carBrand OPEL) (carModel INSIGNIA) (basePrice 700.0))
)

;;***************************
;; Rules for engine capacity*
;;***************************

(defrule engine-capacity-1-0
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.0)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.1))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for engine capacity 1.0") (value "10%")))
)

(defrule engine-capacity-1-2
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.2)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.15))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for engine capacity 1.2") (value "15%")))
)

(defrule engine-capacity-1-4
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.4)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.2))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for engine capacity 1.4") (value "20%")))
)

(defrule engine-capacity-1-6
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.6)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.25))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for engine capacity 1.6") (value "25%")))
)

(defrule engine-capacity-1-8
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 1.8)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.3))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for engine capacity 1.8") (value "30%")))
)

(defrule engine-capacity-2-0
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 2.0)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.35))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for engine capacity 2.0") (value "35%")))
)

(defrule engine-capacity-2-2
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable engineCapacity) (eq ?value 2.2)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.4))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for engine capacity 2.2") (value "40%")))
)

;;***************************
;; Rules for production year*
;;***************************

(defrule production-year-new
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable productionYear) (and (> ?value 2019) (< ?value 2023))))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentDiscount (+ ?ch:percentDiscount 0.09))))
    (assert (price-info (info-type "Percent Discount") (desc "Discount for production year between 2020 and 2022") (value "9%")))
)

(defrule production-year-old
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable productionYear) (and (>= ?value 2000) (<= ?value 2019))))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (percentRise (+ ?ch:percentRise 0.13))))
    (assert (price-info (info-type "Percent Rise") (desc "Rise for production year between 2000 and 2019") (value "13%")))
)

;;************************
;; Rules for licence year*
;;************************

(defrule licence-year
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable licenceYear) (and (>= ?value 1) (<= ?value 5))))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantRise (+ ?ch:constantRise 300.0))))
    (assert (price-info (info-type "Constant Rise") (desc "Rise for having a licence shorter than 5 years") (value "$300.00")))
)

;;********************************
;; Rules for regular customer age*
;;********************************

(defrule regular-customer-age-6-9
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable regularCustomerAge) (and (>= ?value 6) (<= ?value 9))))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantDiscount (+ ?ch:constantDiscount 200.0))))
    (assert (price-info (info-type "Constant Discount") (desc "Discount for regular customer age between 6 9 years") (value "$200.00")))
)

;;************************
;; Rules for had accident*
;;************************

(defrule had-accident-yes
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable hadAccident) (eq ?value YES)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantRise (+ ?ch:constantRise 650.0))))
    (assert (price-info (info-type "Constant Rise") (desc "Rise for having accident") (value "$650.00")))
)

(defrule had-accident-no
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable hadAccident) (eq ?value NO)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantDiscount (+ ?ch:constantDiscount 200.0))))
    (assert (price-info (info-type "Constant Discount") (desc "Discount for not having accident") (value "$200.00")))
)

;;**************************
;; Rules for marital status*
;;**************************

(defrule marital-status-married
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value MARRIED)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantDiscount (+ ?ch:constantDiscount 100.0))))
    (assert (price-info (info-type "Constant Discount") (desc "Discount for being married") (value "$100.00")))
)

(defrule marital-status-divorced
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value DIVORCED)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantDiscount (+ ?ch:constantDiscount 45.0))))
    (assert (price-info (info-type "Constant Discount") (desc "Discount for being divorced") (value "$45.00")))
)

(defrule marital-status-widow
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value WIDOW)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantRise (+ ?ch:constantRise 150.0))))
    (assert (price-info (info-type "Constant Rise") (desc "Rise for being widow") (value "$150.00")))
)

(defrule marital-status-single
    ?f <- (attribute (variable ?variable) (value ?value))
    (test (and (eq ?variable maritalStatus) (eq ?value SINGLE)))
    =>
    (do-for-fact ((?ch price-change)) TRUE (modify ?ch (constantRise (+ ?ch:constantRise 300.0))))
    (assert (price-info (info-type "Constant Rise") (desc "Rise for being single") (value "$300.00")))
)


;;***********
;; Functions*
;;***********

;;calculating insurance for given car brand and car model from base price using global variables
(deffunction calculate-insurance (?carBrand ?carModel ?basePrice)
    (bind ?priceAfter ?basePrice)
    (do-for-all-facts ((?c car)) (and (eq ?c:carBrand ?carBrand) (eq ?c:carModel ?carModel))
    (do-for-fact ((?ch price-change)) TRUE
    (if (> ?ch:constantRise 0.0 ) then (bind ?priceAfter (+ ?priceAfter ?ch:constantRise)))
    (if (> ?ch:constantDiscount 0.0) then (bind ?priceAfter (- ?priceAfter ?ch:constantDiscount)))
    (if (> ?ch:percentDiscount 0.0) then (bind ?priceAfter (* ?priceAfter ?ch:percentDiscount)))
    (if (> ?ch:percentRise 1.0) then (bind ?priceAfter (* ?priceAfter ?ch:percentRise)))
    (assert (price-after (changed-price ?priceAfter))))
))


;;counting same attributes and then deleting duplicates leaving the last one added
(deffunction remove-duplicated-attributes (?variable)
    (bind ?facts (get-fact-list))
    (bind ?count 0)
    (foreach ?f ?facts
        (bind ?index (fact-index ?f))
        (if (eq (fact-relation ?index) attribute) then
            (if (eq (fact-slot-value ?index variable) ?variable)
                then (bind ?count (+ ?count 1))
            )
        )
    )
    (foreach ?f ?facts
        (bind ?index (fact-index ?f))
        (if (eq (fact-relation ?index) attribute) then
            (if (and (eq (fact-slot-value ?index variable) ?variable) (> ?count 1))
                then (retract ?f) (bind ?count (- ?count 1))
            )
        )
    )
)

(deffunction get-price-after-calculations ()
    (bind ?facts (find-all-facts ((?p price-after)) TRUE))
)

(deffunction get-price-info ()
    (bind ?facts (find-all-facts ((?p price-info)) TRUE))
)

(deffunction get-all-cars ()
    (bind ?facts (find-all-facts ((?c car)) TRUE))
)
