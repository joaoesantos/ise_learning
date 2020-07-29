package pt.iselearning.services.domain

import pt.iselearning.services.util.Constants
import javax.persistence.*

/**
 * data class that represents the tag entity
 */
@Entity
@Table(name="tag", schema = Constants.SCHEMA)
data class Tag (
    @Id
    @Column(name="tag_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var tagId : Int? =  null,

    @Column(name="tag")
    var tag : String?
) {
    constructor() : this(null, null)
}
