object Modules {

    object Core {

        const val Domain = ":core:domain"
        const val Presentation = ":core:presentation"
    }

    object Common {

        object Feature {

            object Contacts {

                const val Aidl = ":common:feature:contacts:aidl"
                const val Data = ":common:feature:contacts:data"
                const val Domain = ":common:feature:contacts:domain"
            }
        }
    }

    object Feature {

        object Contacts {

            const val Presentation = ":feature:contacts:presentation"
            const val DuplicatesCleaning = ":feature:contacts:duplicates_cleaning"
        }
    }
}