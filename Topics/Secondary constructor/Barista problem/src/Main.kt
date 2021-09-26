class EspressoMachine {
    val costPerServing: Float

    constructor(coffeeCapsulesCount: Int, totalCost: Float) {
        this.costPerServing = totalCost / coffeeCapsulesCount
    }

    constructor(coffeeBeansWeight: Float, totalCost: Float) {
        this.costPerServing = totalCost / (coffeeBeansWeight / 10)
    }
}
