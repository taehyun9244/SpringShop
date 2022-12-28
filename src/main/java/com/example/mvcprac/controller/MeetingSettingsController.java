package com.example.mvcprac.controller;

import com.example.mvcprac.dto.meeting.MeetingDescriptionForm;
import com.example.mvcprac.dto.tag.TagForm;
import com.example.mvcprac.dto.zone.ZoneForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.model.Zone;
import com.example.mvcprac.repository.TagRepository;
import com.example.mvcprac.repository.ZoneRepository;
import com.example.mvcprac.service.MeetingService;
import com.example.mvcprac.service.TagService;
import com.example.mvcprac.validation.customize.CurrentAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/meeting/{path}/settings")
@RequiredArgsConstructor
public class MeetingSettingsController {

    private final MeetingService meetingService;
    private final ModelMapper modelMapper;
    private final TagService tagService;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/description")
    public String viewMeetingSetting(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        model.addAttribute(modelMapper.map(meeting, MeetingDescriptionForm.class));
        return "meeting/settings/description";
    }

    @PostMapping("/description")
    public String updateMeetingInfo(@CurrentAccount Account account, @PathVariable String path,
                                    @Validated MeetingDescriptionForm studyDescriptionForm, BindingResult bindingResult,
                                    Model model, RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);

        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(meeting);
            return "meeting/settings/description";
        }

        meetingService.updateMeetingDescription(meeting, studyDescriptionForm);
        attributes.addFlashAttribute("message", "교류회 소개를 수정했습니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/description";
    }

    @GetMapping("/banner")
    public String meetingBannerForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        model.addAttribute(modelMapper.map(meeting, MeetingDescriptionForm.class));
        return "meeting/settings/banner";
    }

    @PostMapping("/banner")
    public String meetingBannerSubmit(@CurrentAccount Account account, @PathVariable String path,
                                   String image, RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        meetingService.updateMeetingImage(meeting, image);
        attributes.addFlashAttribute("message", "교류회 이미지를 수정했습니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/banner";
    }

    @PostMapping("/banner/enable")
    public String enableMeetingBanner(@CurrentAccount Account account, @PathVariable String path) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        meetingService.enableMeetingBanner(meeting);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/banner";
    }

    @PostMapping("/banner/disable")
    public String disableStudyBanner(@CurrentAccount Account account, @PathVariable String path) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        meetingService.disableMeetingBanner(meeting);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/banner";
    }

    @GetMapping("/tags")
    public String studyTagsForm(@CurrentAccount Account account, @PathVariable String path, Model model)
            throws JsonProcessingException {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(meeting);

        model.addAttribute("tags", meeting.getTags().stream()
                .map(Tag::getTitle).collect(Collectors.toList()));
        List<String> allTagTitles = tagRepository.findAll().stream()
                .map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTagTitles));
        return "meeting/settings/tags";
    }

    @PostMapping("/tags/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentAccount Account account, @PathVariable String path,
                                 @RequestBody TagForm tagForm) {
        Meeting meeting = meetingService.getMeetingToUpdateTag(account, path);
        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        meetingService.addTag(meeting, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tags/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentAccount Account account, @PathVariable String path,
                                    @RequestBody TagForm tagForm) {
        Meeting meeting = meetingService.getMeetingToUpdateTag(account, path);
        Tag tag = tagRepository.findByTitle(tagForm.getTagTitle());
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        meetingService.removeTag(meeting, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/zones")
    public String studyZonesForm(@CurrentAccount Account account, @PathVariable String path, Model model)
            throws JsonProcessingException {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        model.addAttribute("zones", meeting.getZones().stream()
                .map(Zone::toString).collect(Collectors.toList()));
        List<String> allZones = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));
        return "meeting/settings/zones";
    }

    @PostMapping("/zones/add")
    @ResponseBody
    public ResponseEntity addZone(@CurrentAccount Account account, @PathVariable String path,
                                  @RequestBody ZoneForm zoneForm) {
        Meeting meeting = meetingService.getMeetingToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        meetingService.addZone(meeting, zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/zones/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentAccount Account account, @PathVariable String path,
                                     @RequestBody ZoneForm zoneForm) {
        Meeting meeting = meetingService.getMeetingToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        meetingService.removeZone(meeting, zone);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/meeting")
    public String meetingSettingForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        return "meeting/settings/meeting";
    }

    @PostMapping("/meeting/publish")
    public String publishMeeting(@CurrentAccount Account account, @PathVariable String path,
                               RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        meetingService.publish(meeting);
        attributes.addFlashAttribute("message", "교류회를 공개했습니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
    }

    @PostMapping("/meeting/close")
    public String closeMeeting(@CurrentAccount Account account, @PathVariable String path,
                             RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        meetingService.close(meeting);
        attributes.addFlashAttribute("message", "교류회를 종료했습니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
    }

    @PostMapping("/recruit/start")
    public String startRecruit(@CurrentAccount Account account, @PathVariable String path, Model model,
                               RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        if (!meeting.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
        }

        meetingService.startRecruit(meeting);
        attributes.addFlashAttribute("message", "인원 모집을 시작합니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
    }

    @PostMapping("/recruit/stop")
    public String stopRecruit(@CurrentAccount Account account, @PathVariable String path, Model model,
                              RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        if (!meeting.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
        }

        meetingService.stopRecruit(meeting);
        attributes.addFlashAttribute("message", "인원 모집을 종료합니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
    }

    @PostMapping("/meeting/path")
    public String updateMeetingPath(@CurrentAccount Account account, @PathVariable String path, String newPath,
                                  Model model, RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        if (!meetingService.isValidPath(newPath)) {
            model.addAttribute(account);
            model.addAttribute(meeting);
            model.addAttribute("meetingPathError", "해당 교류회 경로는 사용할 수 없습니다. 다른 값을 입력하세요.");
            return "meeting/settings/meeting";
        }

        meetingService.updateMeetingPath(meeting, newPath);
        attributes.addFlashAttribute("message", "교류회 경로를 수정했습니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
    }

    @PostMapping("/meeting/title")
    public String updateMeetingTitle(@CurrentAccount Account account, @PathVariable String path, String newTitle,
                                   Model model, RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        if (!meetingService.isValidTitle(newTitle)) {
            model.addAttribute(account);
            model.addAttribute(meeting);
            model.addAttribute("meetingTitleError", "교류회 이름을 다시 입력하세요.");
            return "meeting/settings/meeting";
        }

        meetingService.updateMeetingTitle(meeting, newTitle);
        attributes.addFlashAttribute("message", "교류회 이름을 수정했습니다.");
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/settings/meeting";
    }

    @PostMapping("/meeting/remove")
    public String removeMeeting(@CurrentAccount Account account, @PathVariable String path) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        meetingService.remove(meeting);
        return "redirect:/";
    }
}
