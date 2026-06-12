package com.example.springedu2.controller;

import com.example.springedu2.entity.Visitor;
import com.example.springedu2.repository.VisitorRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor // 3. 생성자 주입 다른 방법 시 @추가해야됨, 필수적인 인자만 사용한 생성자
public class VisitorController {

    // 1. @Autowired 이용 생성자 주입
    /* @Autowired
    private VisitorRepository visitorRepository; */

    // 2. 생성자 주입 : 요즘 방식
    /*private VisitorRepository visitorRepository;
    // 생성자 주입
    public VisitorController(VisitorRepository visitorRepository){
        this.visitorRepository  =   visitorRepository;
    } // 생성자 주입, 생성자를 통해서 전역변수로 사용*/

    // 3. 생성자 주입 다른 방법, final 가능
    // @RequiredArgsConstructor 필수 : Lombok를 꼭 써야됨
    private final VisitorRepository visitorRepository;

    // 방명록 조회
    @GetMapping("/vlist")
    public ModelAndView vlist() {
        List<Visitor> visitors        =   visitorRepository.findAll(); // 목록 조회
        return visitorView(visitors, null);
    }

    //
    private ModelAndView visitorView(List<Visitor> visitors, String buttonText ) {
        ModelAndView    mv      =   new ModelAndView("visitorView");
        //mv.setViewName("visitorView"); // visitorView.html(Model사용) - thymeleaf 문법
        if ( visitors.isEmpty() ) { // 조회한 결과가 없다면
            mv.addObject("msg","조회된 결과가 없습니다.");
        } else {
            mv.addObject("vList", visitors);
        }
        if (buttonText != null) {
            mv.addObject("buttonText", buttonText);
        }
        return mv;
    }

    @GetMapping("/vsearch")
    public ModelAndView vsearch(){
        return null;
    }

    // @Valid : form에서 넘어온 자료를 @Entity에 있는 설정
    // (@Id, @NotBlank, @Column(nullable=false))과 비교해서 입력 data를 검증하는 역할
    // data를 check 해서 bindingReuslt에 error message를 넣어줌
    @PostMapping("vinsert")
    @Transactional
    public String vinsert(@Valid Visitor visitor, BindingResult bindingResult, Model model) {
        System.out.println("visitor: " + visitor);
        System.out.println("bindingResult: " + bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("msg","이름과 내용을 모두 입력하세요");
            return "visitorView"; // visitorView.html을 의미함 (temlpates)
        }
        visitorRepository.save(visitor); // entity 객체를 사용해야 한다  , save()의 인자는 무조건 entity type을 넣어줘야 함
        return "redirect:/vlist";
    }
}
