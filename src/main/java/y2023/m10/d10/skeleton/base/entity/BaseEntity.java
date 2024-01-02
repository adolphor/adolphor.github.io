package y2023.m10.d10.skeleton.base.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class BaseEntity implements Serializable {

  @Id
  @NonNull
  private String id;
  @NonNull
  private LocalDateTime createTime;
  @NonNull
  private String createUser;
  @NonNull
  private LocalDateTime updateTime;
  @NonNull
  private String updateUser;

}
