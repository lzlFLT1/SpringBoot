package kasei.boot.web.config.security;

import kasei.boot.web.repository.h2.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;




@Component // 当有一个自定义的 UserDetailsService 类型的 bean 存在于 IOC 容器中时，会关闭 UserDetailsServiceAutoConfiguration 的自动配置
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        kasei.boot.web.repository.h2.entity.User byAccount = userDao.findByAccount(username);
        if (byAccount == null) {
            return null;
        }

        // 为当前用户配置权限
        GrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_USER");
        Collection<? extends GrantedAuthority> authorities = Set.of(authority1);
        // 注意这里的 User 是  org.springframework.security.core.userdetails.User 不是自定义的 User
        UserDetails userDetails = new User(username, byAccount.getPassword(), authorities);
        return userDetails;
    }
}
