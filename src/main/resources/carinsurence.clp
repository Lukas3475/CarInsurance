(defmodule CARS)

(deftemplate CARS::car
    (slot carBrand)
    (slot carModel)
    (slot basePrice)
)

(deftemplate CARS::production-year-perc

(deffacts CARS::car-list
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
    (car (carBrand OPEL) (carModel INSYGNIA) (basePrice 700))
)

