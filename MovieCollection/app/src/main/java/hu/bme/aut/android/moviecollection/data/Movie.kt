package hu.bme.aut.android.moviecollection.data

import androidx.room.*

@Entity(tableName = "movie")
data class Movie(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "year") var year: Int,
    @ColumnInfo(name = "length") var length: Int,
    @ColumnInfo(name = "genre") var genre: Genre,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "is_seen") var isSeen: Boolean
)
{
    enum class Genre (val alt: String) {
        ACTION("Action"),
        COMEDY("Comedy"),
        DRAMA("Drama"),
        FANTASY("Fantasy"),
        HORROR("Horror"),
        ROMANCE("Romance"),
        SCI_FI("Sci-fi");

        fun getAltName() : String {
            return alt
        }
        companion object {
            @JvmStatic
            /**Alapvetően, amikor a companion object-ek Jvm bájtkódra fordulnak,
             * akkor egy külön statikus osztály jön számukra létre.
             * Ezzel az annotációval lehet megadni, hogy ne jöjjön létre:   */
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Genre? {
                var ret: Genre? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(genre: Genre): Int {
                return genre.ordinal
            }
        }
    }
}
