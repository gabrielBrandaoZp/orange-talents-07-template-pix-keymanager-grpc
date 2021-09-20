package br.com.zup.edu.keymanager

enum class PixType {
    CPF {
        override fun validation(value: String?): Boolean {
            if (value.isNullOrBlank()) {
                return false
            }

            if (!value.matches("^[0-9]{11}".toRegex())) {
                return false
            }

            return value.matches("^[0-9]{11}".toRegex())
        }
    },
    EMAIL {
        override fun validation(value: String?): Boolean {
            if (value.isNullOrBlank()) {
                return false
            }

            return value.matches("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$".toRegex())
        }
    },
    TELEFONE {
        override fun validation(value: String?): Boolean {
            if (value.isNullOrBlank()) {
                return false
            }

            return value.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    CHAVE_ALEATORIA {
        override fun validation(value: String?): Boolean {
            //this key will be autogenerated and your value can be blank
            return value.isNullOrBlank()
        }
    };

    abstract fun validation(value: String?): Boolean
}