package kr.easw.drforestspringkt.annotations

/** "베타 기능" 표시 어노테이션.
     해당 어노테이션이 사용된 클래스, 펑션,혹은 필드는 해당 기능이 베타 상태의 기능이며, 추후 변경될 확률이 상당히 높다는것을 알립니다.
    베타 기능이 적용된 코드는 해당 기능의 코드가 변경될 경우, 사용법이나 파라미터등이 완전히 바뀌어 기존 코드를 재작성해야할 수도 있습니다.
*/
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Beta